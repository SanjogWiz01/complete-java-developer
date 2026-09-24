/*
 * 02_Cookies_Read_Delete_Securely.java
 *
 * Demonstrates:
 * 1. Reading a cookie
 * 2. Deleting a cookie
 * 3. HttpOnly and Secure flags
 * 4. SameSite configuration note
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cookies/manage")
public class CookiesManageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        Cookie found = findCookie(request, "username");

        if (found != null) {
            response.getWriter().println("Cookie value = " + found.getValue());
        } else {
            response.getWriter().println("Cookie not found.");
        }

        response.getWriter().println("\nTo delete it, call /cookies/manage?delete=true");

        if ("true".equals(request.getParameter("delete"))) {
            Cookie delete = new Cookie("username", "");
            delete.setMaxAge(0);
            delete.setPath("/");
            delete.setHttpOnly(true);
            response.addCookie(delete);
            response.getWriter().println("Cookie deletion response sent.");
        }
    }

    private Cookie findCookie(HttpServletRequest request, String name) {
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

/*
 * Security:
 * - HttpOnly helps reduce cookie theft through XSS.
 * - Secure means the browser sends the cookie only over HTTPS.
 * - SameSite controls cross-site cookie sending.
 * - Never trust cookie values; validate them on the server.
 *
 * Spring Boot can configure session-cookie SameSite using:
 * server.servlet.session.cookie.same-site=lax
 */
