/*
 * CookieSession24.java
 *
 * Topic: The Secure flag and cookie lifetime.
 *
 * Secure   the browser sends the cookie only over HTTPS.
 * MaxAge   lifetime in seconds; a positive value persists across browser
 *          restarts, zero or negative deletes the cookie immediately.
 *
 * A session cookie omits MaxAge entirely and is discarded when the browser
 * closes. Persistent cookies survive restarts, so prefer short lifetimes.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

@WebServlet("/cookies/secure")
public class CookieSession24 extends HttpServlet {

    private static final String PERSISTENT = "persistentToken";
    private static final String SESSION_ONLY = "sessionToken";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String action = request.getParameter("action");

        if ("set".equals(action)) {
            long maxAge = Duration.ofMinutes(15).toSeconds();

            Cookie persistent = new Cookie(PERSISTENT, "abc123");
            persistent.setMaxAge((int) maxAge);
            persistent.setPath("/");
            persistent.setHttpOnly(true);
            persistent.setSecure(true);
            response.addCookie(persistent);

            Cookie sessionOnly = new Cookie(SESSION_ONLY, "xyz789");
            sessionOnly.setMaxAge(-1);
            sessionOnly.setPath("/");
            sessionOnly.setHttpOnly(true);
            sessionOnly.setSecure(true);
            response.addCookie(sessionOnly);

            response.getWriter().println("persistentToken  Max-Age=" + maxAge + "  Secure");
            response.getWriter().println("sessionToken     Max-Age=-1     Secure");
            return;
        }

        if ("clear".equals(action)) {
            for (String name : new String[] { PERSISTENT, SESSION_ONLY }) {
                Cookie dead = new Cookie(name, "");
                dead.setMaxAge(0);
                dead.setPath("/");
                response.addCookie(dead);
            }
            response.getWriter().println("Both cookies were expired.");
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            response.getWriter().println("No cookies received. Try /cookies/secure?action=set");
            return;
        }

        for (Cookie cookie : cookies) {
            if (PERSISTENT.equals(cookie.getName()) || SESSION_ONLY.equals(cookie.getName())) {
                response.getWriter().println(cookie.getName() + " = " + cookie.getValue());
            }
        }

        response.getWriter().println();
        response.getWriter().println("Request is secure = " + request.isSecure());
    }
}
