/*
 * 04_HttpSession_Login_Logout.java
 *
 * Simplified educational login/logout example.
 * Real applications should use Spring Security rather than writing
 * authentication logic manually.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/session/auth")
public class SessionAuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        if ("logout".equals(request.getParameter("action"))) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.getWriter().println("Logged out.");
            return;
        }

        String user = request.getParameter("user");

        if (user != null && !user.isBlank()) {
            HttpSession oldSession = request.getSession(false);

            // Educational session-fixation mitigation:
            // invalidate old session before creating a new authenticated session.
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("authenticatedUser", user);

            response.getWriter().println("Logged in as: " + user);
            response.getWriter().println("New session ID: " + newSession.getId());
        } else {
            HttpSession session = request.getSession(false);

            if (session == null) {
                response.getWriter().println("No active session.");
                return;
            }

            Object authenticatedUser = session.getAttribute("authenticatedUser");

            if (authenticatedUser == null) {
                response.getWriter().println("Not authenticated.");
            } else {
                response.getWriter().println("Authenticated user: " + authenticatedUser);
            }
        }
    }
}
