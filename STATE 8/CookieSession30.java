/*
 * CookieSession30.java
 *
 * Topic: Cookies versus sessions.
 *
 * Cookies live in the browser and travel on every request, so they are
 * small, visible to the user, and tamperable. Sessions live on the server,
 * so they hold more data, stay out of reach of the client, and cost server
 * memory or storage.
 *
 * The usual split is: session cookie for the session id, persistent cookie
 * for a long-lived "remember me" token, and a database for the data itself.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@WebServlet("/cookies/versus-session")
public class CookieSession30 extends HttpServlet {

    private static final String REMEMBER_ME = "rememberMe";
    private static final String USER_KEY = "username";
    private static final Duration REMEMBER_ME_LIFETIME = Duration.ofDays(14);
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        if ("remember".equals(action)) {
            issueRememberMe(response, String.valueOf(session.getAttribute(USER_KEY)));
            response.getWriter().println("Remember-me token issued for "
                    + REMEMBER_ME_LIFETIME.toDays() + " days.");
        } else if ("forget".equals(action)) {
            Cookie dead = new Cookie(REMEMBER_ME, "");
            dead.setMaxAge(0);
            dead.setPath("/");
            response.addCookie(dead);
            response.getWriter().println("Remember-me token cleared.");
        }

        String sessionUser = (String) session.getAttribute(USER_KEY);
        String cookieUser = readRememberMe(request);

        response.getWriter().println();
        response.getWriter().println("session id   = " + session.getId());
        response.getWriter().println("session user = " + (sessionUser == null ? "<none>" : sessionUser));
        response.getWriter().println("cookie user  = " + (cookieUser == null ? "<none>" : "token present"));
        response.getWriter().println();
        response.getWriter().println("Cookies are sent on every matching request and are editable by the user.");
        response.getWriter().println("Session data stays on the server and dies with the session.");
    }

    private void issueRememberMe(HttpServletResponse response, String user) {
        byte[] token = new byte[32];
        RANDOM.nextBytes(token);

        Cookie cookie = new Cookie(REMEMBER_ME, Base64.getUrlEncoder().withoutPadding().encodeToString(token));
        cookie.setMaxAge((int) REMEMBER_ME_LIFETIME.toSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }

    private String readRememberMe(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (REMEMBER_ME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
