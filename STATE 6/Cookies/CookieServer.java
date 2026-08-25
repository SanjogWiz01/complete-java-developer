import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Cookie Server (Java 8, JDK built-in HTTP server - no Tomcat needed)
 * -------------------------------------------------------------------
 * Endpoints:
 *   GET /                -> shows every cookie the browser sent back
 *   GET /visit           -> visit counter kept entirely in a cookie
 *   GET /set?name=x      -> creates/updates cookie "x" (1 hour); optional &value=y
 *   GET /delete?name=x   -> removes cookie "name"
 *
 * Try it:
 *   javac CookieImplementation.java CookieServer.java
 *   java CookieServer
 *   then open  http://localhost:8085/  in a browser, or:
 *   curl -i "http://localhost:8085/set?name=user&value=sanjog"
 */
public class CookieServer {

    static final int PORT = 8085;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HomePage());
        server.createContext("/visit", new VisitCounter());
        server.createContext("/set", new SetCookieHandler());
        server.createContext("/delete", new DeleteCookieHandler());
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();
        System.out.println("Cookie server running on http://localhost:" + PORT);
        System.out.println("Try: /set?name=theme&value=dark   |   /visit   |   /delete?name=theme");
    }

    /* ============================ handlers ============================ */

    /** Lists all cookies the client returned to us. */
    static class HomePage implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> cookies = readCookies(ex);
            StringBuilder page = new StringBuilder();
            page.append("<h1>Cookies your browser sent</h1>");
            if (cookies.isEmpty()) {
                page.append("<p>No cookies yet. Create one with ")
                    .append("<a href=\"/set?name=user&amp;value=sanjog\">/set?name=user&amp;value=sanjog</a></p>");
            } else {
                page.append("<table border='1'>")
                    .append("<tr><th>Name</th><th>Value</th></tr>");
                cookies.forEach((k, v) ->
                        page.append("<tr><td>").append(esc(k)).append("</td><td>")
                            .append(esc(v)).append("</td></tr>"));
                page.append("</table>");
            }
            sendHtml(ex, 200, page.toString());
        }
    }

    /** Stateless visit counter - state lives in the browser's cookie. */
    static class VisitCounter implements HttpHandler {
        private static final String COOKIE = "visits";

        @Override
        public void handle(HttpExchange ex) throws IOException {
            int visits = readCookies(ex).entrySet().stream()
                    .filter(e -> e.getKey().equals(COOKIE))
                    .mapToInt(e -> parseIntSafe(e.getValue()))
                    .findFirst()
                    .orElse(0) + 1;

            // Java 8 streams: decide greeting based on the count.
            String message = visits == 1 ? "Welcome, first visit!"
                           : visits < 5  ? "Visit number " + visits
                                         : "Wow, visit number " + visits;

            ex.getResponseHeaders().add("Set-Cookie",
                    CookieImplementation.build(COOKIE, String.valueOf(visits), 3600, "/", true));
            sendHtml(ex, 200, "<h1>" + esc(message) + "</h1><p>Refresh to watch it grow.</p>"
                    + "<p><a href='/'>back home</a></p>");
        }
    }

    /** Creates or updates a cookie from the query string (?name=value). */
    static class SetCookieHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> query = parseQuery(ex.getRequestURI().getRawQuery());
            Optional<String> name  = Optional.ofNullable(query.get("name"));
            Optional<String> value = Optional.ofNullable(query.getOrDefault("value", ""));

            String body;
            if (!name.isPresent() || name.get().isEmpty()) {
                body = "<h1 style='color:red'>Missing ?name= parameter</h1>";
                sendHtml(ex, 400, body);
                return;
            }
            ex.getResponseHeaders().add("Set-Cookie",
                    CookieImplementation.build(name.get(), value.orElse(""), 3600, "/", true));
            body = "<h1>Cookie created</h1><p>" + esc(name.get()) + " = "
                    + esc(value.orElse("")) + " (expires in 1 hour)</p><a href='/'>check home</a>";
            sendHtml(ex, 200, body);
        }
    }

    /** Deletes a cookie by telling the browser Max-Age=0. */
    static class DeleteCookieHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> query = parseQuery(ex.getRequestURI().getRawQuery());
            String name = query.get("name");
            if (name == null || name.isEmpty()) {
                sendHtml(ex, 400, "<h1 style='color:red'>Missing ?name= parameter</h1>");
                return;
            }
            ex.getResponseHeaders().add("Set-Cookie", CookieImplementation.deleteCookie(name));
            sendHtml(ex, 200, "<h1>Cookie '" + esc(name) + "' deleted</h1><a href='/'>check home</a>");
        }
    }

    /* ============================ helpers ============================ */

    /** Extracts every cookie from the request's "Cookie" headers. */
    static Map<String, String> readCookies(HttpExchange ex) {
        return ex.getRequestHeaders().getOrDefault("Cookie", Collections.emptyList()).stream()
                .flatMap(h -> CookieImplementation.parseRequestHeader(h).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                          (a, b) -> b, LinkedHashMap::new));
    }

    static void sendHtml(HttpExchange ex, int status, String bodyHtml) throws IOException {
        byte[] raw = ("<html><body>" + bodyHtml + "</body></html>")
                .getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        ex.sendResponseHeaders(status, raw.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(raw);
        }
    }

    static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> map = new HashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) {
            return map;
        }
        for (String pair : rawQuery.split("&")) {
            int eq = pair.indexOf('=');
            if (eq > 0) {
                map.put(decodeUtf8(pair.substring(0, eq)),
                        decodeUtf8(pair.substring(eq + 1)));
            }
        }
        return map;
    }

    static String decodeUtf8(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            return s;
        }
    }

    static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
