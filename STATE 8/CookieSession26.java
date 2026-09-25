/*
 * CookieSession26.java
 *
 * Topic: Working with session attributes safely.
 *
 * A session attribute map is a plain object map, so the compiler will not
 * remind you that a cast may fail. Always null-check after casting, and
 * prefer the getOrDefault pattern over a bare cast.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/session/attributes")
public class CookieSession26 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        HttpSession session = request.getSession();

        if ("true".equals(request.getParameter("clear"))) {
            for (String name : session.getAttributeNames().toArray(new String[0])) {
                session.removeAttribute(name);
            }
            response.getWriter().println("All session attributes were removed.");
            return;
        }

        Object visits = session.getAttribute("visits");
        int count = visits instanceof Integer ? (Integer) visits : 0;
        session.setAttribute("visits", count + 1);

        session.setAttribute("username", "sanjog");
        session.setAttribute("roles", List.of("USER", "ADMIN"));

        Object roles = session.getAttribute("roles");
        List<String> safeRoles = roles instanceof List ? castToStringList(roles) : new ArrayList<>();

        response.getWriter().println("visits  = " + count);
        response.getWriter().println("username = " + session.getAttribute("username"));
        response.getWriter().println("roles    = " + safeRoles);
        response.getWriter().println("missing  = " + session.getAttribute("doesNotExist"));
        response.getWriter().println("names    = " + session.getAttributeNames());
    }

    @SuppressWarnings("unchecked")
    private List<String> castToStringList(Object value) {
        return (List<String>) value;
    }
}
