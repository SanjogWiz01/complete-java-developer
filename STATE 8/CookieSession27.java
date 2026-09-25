/*
 * CookieSession27.java
 *
 * Topic: Session fixation protection.
 *
 * Session fixation happens when an attacker plants a known session id in
 * the victim's browser and waits for the victim to authenticate with it.
 * The fix is to issue a brand new session id at the moment of login.
 *
 * Servlet 3.1 and later provide changeSessionId(), which rotates the id
 * while keeping the existing attributes intact.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/session/fixation")
public class CookieSession27 extends HttpServlet {

    private static final String AUTHENTICATED = "authenticated";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession();
        String before = session.getId();

        if ("true".equals(request.getParameter("login"))) {
            session.setAttribute(AUTHENTICATED, true);
            session.setAttribute("username", "sanjog");

            if (request.isRequestedSessionIdFromCookie() || request.isRequestedSessionIdValid()) {
                session.changeSessionId();
            } else {
                response.addHeader("X-Session-Rotation", "new-session");
            }
        }

        String after = session.getId();

        response.getWriter().println("id before = " + before);
        response.getWriter().println("id after  = " + after);
        response.getWriter().println("rotated   = " + !before.equals(after));
        response.getWriter().println("auth      = " + session.getAttribute(AUTHENTICATED));
        response.getWriter().println();
        response.getWriter().println("Call /session/fixation?login=true to rotate the session id.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        response.sendRedirect(request.getContextPath() + "/session/fixation?login=true");
    }
}
