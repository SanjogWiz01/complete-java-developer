/*
 * CookieSession33.java
 *
 * Topic: Cookie size limits and browser quotas.
 *
 * RFC 6265 only requires supporting 4096 bytes per cookie. Browsers also
 * cap how many cookies a single domain may hold, and a server response
 * header has a finite buffer, so oversized cookies are dropped without
 * warning.
 *
 * A useful rule of thumb: keep every cookie under 4 KB and stay well under
 * 20 cookies per domain.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/cookies/limits")
public class CookieSession33 extends HttpServlet {

    private static final int RFC_LIMIT_BYTES = 4096;
    private static final int MAX_REQUESTED = 6000;
    private static final String COOKIE_NAME = "payload";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        int requested = 1000;
        String requestedRaw = request.getParameter("size");
        if (requestedRaw != null) {
            try {
                requested = Integer.parseInt(requestedRaw);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("size must be a number");
                return;
            }
        }
        requested = Math.max(1, Math.min(requested, MAX_REQUESTED));

        String value = "x".repeat(requested);
        int bytes = cookieBytes(COOKIE_NAME, value);

        response.getWriter().println("name=value bytes = " + bytes);
        response.getWriter().println("RFC 6265 minimum  = " + RFC_LIMIT_BYTES);

        if (bytes > RFC_LIMIT_BYTES) {
            response.getWriter().println("verdict            = TOO LARGE, browsers will drop this cookie");
        } else {
            Cookie cookie = new Cookie(COOKIE_NAME, value);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            response.getWriter().println("verdict            = sent, and it came back as "
                    + (readBack(request) == null ? "absent" : "present"));
        }

        response.getWriter().println();
        response.getWriter().println("Typical per-domain caps: Chrome 180, Firefox 150, Safari memory bound.");
        response.getWriter().println("Try /cookies/limits?size=4000 or ?size=5000");
    }

    private int cookieBytes(String name, String value) {
        return (name + "=" + value).getBytes(StandardCharsets.UTF_8).length;
    }

    private String readBack(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
