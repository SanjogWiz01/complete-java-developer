/*
 * CookieSession25.java
 *
 * Topic: Session timeout.
 *
 * setMaxInactiveInterval controls how long a session survives without
 * incoming requests. Calling an activity-bearing method such as
 * getAttribute or setAttribute resets that counter.
 *
 * A default can be set in Spring Boot with:
 *   server.servlet.session.timeout=30m
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/session/timeout")
public class CookieSession25 extends HttpServlet {

    private static final int IDLE_LIMIT_SECONDS = 300;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession();

        int previous = (Integer) session.getAttribute("visits");
        int visits = previous == null ? 1 : previous + 1;
        session.setAttribute("visits", visits);
        session.setMaxInactiveInterval(IDLE_LIMIT_SECONDS);

        long lastAccess = session.getLastAccessedTime();
        long now = System.currentTimeMillis();

        response.getWriter().println("Session id      = " + session.getId());
        response.getWriter().println("New session     = " + session.isNew());
        response.getWriter().println("Visits          = " + visits);
        response.getWriter().println("Idle limit      = " + IDLE_LIMIT_SECONDS + "s");
        response.getWriter().println("Idle for        = " + (now - lastAccess) / 1000 + "s");

        if ("true".equals(request.getParameter("invalidate"))) {
            session.invalidate();
            response.getWriter().println();
            response.getWriter().println("Session was invalidated. A new one is created on the next request.");
        } else {
            response.getWriter().println();
            response.getWriter().println("Call /session/timeout?invalidate=true to end the session now.");
        }
    }
}
