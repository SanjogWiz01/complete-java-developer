/*
 * CookieSession32.java
 *
 * Topic: CSRF protection using the double-submit cookie pattern.
 *
 * The server writes a random token into a cookie and also expects the same
 * token back on every state-changing request. A cross-site form cannot read
 * the cookie, so it cannot produce a matching token.
 *
 * The token must be compared in constant time and must never be readable
 * by JavaScript on the origin that accepts it.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@WebServlet("/csrf/double-submit")
public class CookieSession32 extends HttpServlet {

    private static final String CSRF_COOKIE = "XSRF-TOKEN";
    private static final String CSRF_FIELD = "csrfToken";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String token = readCookie(request);
        if (token == null) {
            token = newToken();
            Cookie cookie = new Cookie(CSRF_COOKIE, token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "Strict");
            response.addCookie(cookie);
        }

        response.getWriter().println("CSRF cookie issued.");
        response.getWriter().println("Echo it back as form field '" + CSRF_FIELD + "' on POST.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String fromCookie = readCookie(request);
        String fromRequest = request.getParameter(CSRF_FIELD);

        if (fromCookie == null || fromRequest == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Rejected: missing CSRF token.");
            return;
        }

        if (!constantTimeEquals(fromCookie, fromRequest)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Rejected: token mismatch.");
            return;
        }

        response.getWriter().println("Accepted: tokens matched.");

        Cookie cleared = new Cookie(CSRF_COOKIE, "");
        cleared.setMaxAge(0);
        cleared.setPath("/");
        response.addCookie(cleared);
    }

    private String newToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private boolean constantTimeEquals(String left, String right) {
        return MessageDigest.isEqual(
                left.getBytes(StandardCharsets.UTF_8),
                right.getBytes(StandardCharsets.UTF_8));
    }

    private String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (CSRF_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
