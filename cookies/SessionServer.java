import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * Session Server (Java 8, JDK built-in HTTP server - no Tomcat needed)
 * --------------------------------------------------------------------
 * Combines CookieImplementation (cookie transport) with SessionManager
 * (server-side memory) to simulate a real login flow:
 *
 *   GET /login?user=sanjog  -> creates session, sends JSESSIONID cookie
 *   GET /                   -> greets logged-in user or shows login link
 *   GET /cart               -> reads session attributes (items in cart)
 *   GET /logout             -> destroys session and deletes the cookie
 *
 * Key difference from CookieServer.java:
 *   the BROWSER only stores a random ID - all real state stays on the server.
 */
public class SessionServer {

    static final int PORT = 8086;
    private static final String SESSION_COOKIE = "JSESSIONID";

    /** Single shared session store for the whole application. */
    private static final SessionManager SESSIONS = new SessionManager();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/cart", new CartHandler());
        server.createContext("/logout", new LogoutHandler());
        server.setExecutor(Executors.newFixedThreadPool(4));

        // Housekeeping thread: sweep expired sessions once per minute (Java 8 lambda).
        server.getExecutor().execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(60_000);
                    int removed = SESSIONS.purgeExpired();
                    if (removed > 0) {
                        System.out.println("[janitor] expired sessions removed: " + removed);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        server.start();
        System.out.println("Session server running on http://localhost:" + PORT);
        System.out.println("Try: /login?user=sanjog then refresh / and open /cart");
    }

    /* ============================ handlers ============================ */

    /** Shows who is logged in by looking up the JSESSIONID cookie. */
    static class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Optional<SessionManager.Session> session = currentSession(ex);
            if (session.isPresent()) {
                String user = String.valueOf(session.get().attributes().getOrDefault("user", "guest"));
                sendHtml(ex, 200,
                        "<h1>Hello, " + esc(user) + "!</h1>"
                        + "<p>Session id: <code>" + esc(session.get().getId()) + "</code></p>"
                        + "<p><a href='/cart'>your cart</a> | <a href='/logout'>logout</a></p>");
            } else {
                sendHtml(ex, 200,
                        "<h1>You are not logged in</h1>"
                        + "<p><a href='/login?user=sanjog'>Log in as sanjog</a></p>");
            }
        }
    }

    /** Creates a session + sets the JSESSIONID cookie on the response. */
    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> query = QueryUtil.parse(ex.getRequestURI().getRawQuery());
            String user = query.getOrDefault("user", "guest");

            SessionManager.Session session = SESSIONS.create();
            session.attributes().put("user", user);
            session.attributes().put("loginCount", 1);

            ex.getResponseHeaders().add("Set-Cookie",
                    CookieImplementation.build(SESSION_COOKIE, session.getId(), -1, "/", true));
            sendHtml(ex, 200,
                    "<h1>Logged in as " + esc(user) + "</h1>"
                    + "<p>Your browser now holds only an opaque ID; your data lives server-side.</p>"
                    + "<a href='/'>home</a> | <a href='/cart'>cart</a>");
        }
    }

    /** Reads data back out of the session. */
    static class CartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Optional<SessionManager.Session> session = currentSession(ex);
            if (!session.isPresent()) {
                ex.getResponseHeaders().set("Location", "/");
                ex.sendResponseHeaders(302, -1);            // redirect to home
                return;
            }
            Object items = session.get().attributes().compute("cartItems",
                    (k, v) -> v == null ? 1 : ((Integer) v) + 1);
            sendHtml(ex, 200,
                    "<h1>" + esc(String.valueOf(session.get().attributes().get("user")))
                            + "'s cart</h1>"
                    + "<p>Items: " + esc(String.valueOf(items)) + " (refresh to add more)</p>"
                    + "<a href='/'>home</a>");
        }
    }

    /** Invalidates the session and removes the browser cookie. */
    static class LogoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            readCookieValue(ex).ifPresent(SESSIONS::invalidate);
            ex.getResponseHeaders().add("Set-Cookie",
                    CookieImplementation.deleteCookie(SESSION_COOKIE));
            sendHtml(ex, 200, "<h1>Logged out - session destroyed.</h1><a href='/'>home</a>");
        }
    }

    /* ============================ helpers ============================ */

    private static Optional<SessionManager.Session> currentSession(HttpExchange ex) {
        return readCookieValue(ex).flatMap(SESSIONS::find);
    }

    private static Optional<String> readCookieValue(HttpExchange ex) {
        return Optional.ofNullable(ex.getRequestHeaders().getFirst("Cookie"))
                .map(h -> CookieImplementation.parseRequestHeader(h).get(SESSION_COOKIE));
    }

    private static void sendHtml(HttpExchange ex, int status, String bodyHtml) throws IOException {
        byte[] raw = ("<html><body>" + bodyHtml + "</body></html>")
                .getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(status, raw.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(raw);
        }
    }

    private static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Tiny query-string helper kept here so SessionServer is self-contained apart from the two shared classes. */
    static final class QueryUtil {
        private QueryUtil() { }

        static Map<String, String> parse(String rawQuery) {
            Map<String, String> map = new HashMap<>();
            if (rawQuery == null || rawQuery.isEmpty()) {
                return map;
            }
            for (String pair : rawQuery.split("&")) {
                int eq = pair.indexOf('=');
                if (eq > 0) {
                    try {
                        map.put(java.net.URLDecoder.decode(pair.substring(0, eq), "UTF-8"),
                                java.net.URLDecoder.decode(pair.substring(eq + 1), "UTF-8"));
                    } catch (java.io.UnsupportedEncodingException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
            return map;
        }
    }
}
