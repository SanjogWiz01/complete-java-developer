/*
 * 03_HttpSession_Basics.java
 *
 * Topic: HttpSession.
 *
 * A session stores server-side state associated with a browser/client.
 * Usually the browser receives a session identifier cookie such as JSESSIONID.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/session/basic")
public class HttpSessionBasicsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession();

        Integer count = (Integer) session.getAttribute("count");
        if (count == null) {
            count = 0;
        }

        count++;
        session.setAttribute("count", count);

        response.getWriter().println("Session ID: " + session.getId());
        response.getWriter().println("Visit count: " + count);
        response.getWriter().println("New session: " + session.isNew());
    }
}
