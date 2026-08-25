import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Classic Servlet Cookie & Session Example (Java 8 / Servlet 3.0+)
 * ----------------------------------------------------------------
 * The same ideas as CookieServer/SessionServer, expressed the way a
 * real Tomcat application writes them with javax.servlet APIs:
 *
 *   - Cookie creation:        new Cookie(name, value), setMaxAge, setPath
 *   - Cookie reading:         request.getCookies()
 *   - Cookie deletion:        setMaxAge(0) + addCookie
 *   - Session tracking:       request.getSession() + attributes
 *
 * Deploy: copy the compiled class into WEB-INF/classes of any Servlet 3.0+
 * webapp on Tomcat 8/9 (javax.* namespace), then visit:
 *     http://localhost:8080/yourapp/cookiedemo?theme=dark
 */
@WebServlet("/cookiedemo")
public class CookieSessionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String THEME_COOKIE = "theme";
    private static final List<String> ALLOWED_THEMES = Arrays.asList("light", "dark", "blue");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        /* ---------- 1. COOKIE CREATION --------------------------------
           Save ?theme=... as a persistent cookie valid for 7 days. */
        String requestedTheme = request.getParameter("theme");
        if (requestedTheme != null && ALLOWED_THEMES.contains(requestedTheme)) {
            Cookie themeCookie = new Cookie(THEME_COOKIE, requestedTheme);
            themeCookie.setMaxAge(7 * 24 * 60 * 60);   // seconds
            themeCookie.setPath("/");
            themeCookie.setHttpOnly(true);
            response.addCookie(themeCookie);           // becomes Set-Cookie header
        }

        /* ---------- 2. COOKIE READING ---------------------------------
           Browsers return every matching cookie in one array. */
        String currentTheme = findCookieValue(request, THEME_COOKIE).orElse("unset");

        /* ---------- 3. SESSION TRACKING -------------------------------
           getSession(true) creates one if absent; container manages the
           JSESSIONID cookie for us. Attributes live ONLY server-side. */
        HttpSession session = request.getSession();
        Integer visits = (Integer) session.getAttribute("visits");
        visits = (visits == null) ? 1 : visits + 1;
        session.setAttribute("visits", visits);
        session.setAttribute("user", "sanjog");
        session.setMaxInactiveInterval(30 * 60);       // idle timeout, seconds

        /* ---------- 4. RENDER ---------------------------------------- */
        out.println("<html><body>");
        out.println("<h1>Servlet Cookies &amp; Sessions</h1>");
        out.println("<p>Theme cookie: <b>" + escape(currentTheme) + "</b>"
                + " &nbsp;(change with ?theme=dark|light|blue)</p>");
        out.println("<p>Session id: <code>" + session.getId() + "</code></p>");
        out.println("<p>Visits this session: <b>" + visits + "</b></p>");
        out.println("<p>User attribute: " + escape(String.valueOf(session.getAttribute("user"))) + "</p>");

        /* ---------- 5. COOKIE DELETION via ?forgetTheme --------------- */
        if (request.getParameter("forgetTheme") != null) {
            Cookie kill = new Cookie(THEME_COOKIE, "");
            kill.setMaxAge(0);                         // browser deletes it now
            kill.setPath("/");
            response.addCookie(kill);
            out.println("<p style='color:red'>Theme cookie deleted.</p>");
        }
        out.println("</body></html>");
    }

    /** Small helper mirroring what containers do internally. */
    static Optional<String> findCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();       // null when no cookies sent
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
