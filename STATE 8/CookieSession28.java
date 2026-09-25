/*
 * CookieSession28.java
 *
 * Topic: Tracking active sessions.
 *
 * A registry of live sessions lets an application show "who is logged in",
 * force a logout, or cap how many sessions a single account may hold.
 *
 * Session creation and destruction events arrive through
 * HttpSessionListener, so no per-request bookkeeping is needed.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/session/registry")
public class CookieSession28 extends HttpServlet {

    static final Map<String, String> ACTIVE = new ConcurrentHashMap<>();

    static void remember(HttpSession session, String user) {
        ACTIVE.put(session.getId(), user);
    }

    static void forget(HttpSession session) {
        ACTIVE.remove(session.getId());
    }

    static void forgetAllFor(String user) {
        ACTIVE.values().removeIf(user::equals);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        HttpSession session = request.getSession();

        if ("true".equals(request.getParameter("login"))) {
            String user = request.getParameter("user");
            if (user == null || user.isBlank()) {
                user = "sanjog";
            }
            remember(session, user);
            response.getWriter().println("Logged in as " + user);
        } else if ("true".equals(request.getParameter("logoutOthers"))) {
            String user = request.getParameter("user");
            if (user == null || user.isBlank()) {
                user = "sanjog";
            }
            forgetAllFor(user);
            response.getWriter().println("Signed " + user + " out everywhere else");
        } else if ("true".equals(request.getParameter("logout"))) {
            forget(session);
            session.invalidate();
            response.getWriter().println("Current session invalidated");
        } else {
            response.getWriter().println("this session = " + session.getId());
        }

        response.getWriter().println();
        response.getWriter().println("Active sessions (" + ACTIVE.size() + "):");
        for (Map.Entry<String, String> entry : ACTIVE.entrySet()) {
            response.getWriter().println("  " + entry.getValue() + "  " + entry.getKey());
        }
    }
}
