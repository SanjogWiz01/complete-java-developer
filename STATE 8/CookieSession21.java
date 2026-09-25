/*
 * CookieSession21.java
 *
 * Topic: Cookie value encoding.
 *
 * Cookie values may not contain arbitrary characters. Commas, semicolons,
 * spaces and non-ASCII text must be encoded before they are written and
 * decoded when they are read back.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/cookies/encoding")
public class CookieSession21 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        if ("true".equals(request.getParameter("write"))) {
            String raw = request.getParameter("value");
            if (raw == null) {
                raw = "Sanjog, Kumar; role=admin";
            }
            response.addCookie(encoded("profile", raw));
            response.getWriter().println("Encoded cookie 'profile' was sent.");
            return;
        }

        Cookie profile = read(request, "profile");
        if (profile == null) {
            response.getWriter().println("No profile cookie yet. Try /cookies/encoding?write=true");
            return;
        }

        response.getWriter().println("Decoded value = " + decode(profile.getValue()));
    }

    private Cookie encoded(String name, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        String safe = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        Cookie cookie = new Cookie(name, safe);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    private String decode(String value) {
        byte[] bytes = Base64.getUrlDecoder().decode(value);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String urlRoundTrip(String value) {
        try {
            return URLDecoder.decode(
                    URLEncoder.encode(value, StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return "<undecodable>";
        }
    }

    private Cookie read(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}
