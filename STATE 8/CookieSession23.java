/*
 * CookieSession23.java
 *
 * Topic: The SameSite cookie attribute.
 *
 * SameSite=Lax    cookie is not sent on cross-site subrequests, but is sent
 *                on a top-level navigation such as clicking a link.
 * SameSite=Strict cookie is never sent on any cross-site request.
 * SameSite=None    cookie is sent cross-site, but requires the Secure flag.
 *
 * SameSite is the main defence against CSRF for session cookies.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cookies/samesite")
public class CookieSession23 extends HttpServlet {

    private static final String COOKIE_NAME = "samesiteDemo";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String mode = request.getParameter("mode");
        if (mode == null) {
            mode = "Lax";
        }

        if (!isValidMode(mode)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("mode must be one of Strict, Lax, None");
            return;
        }

        Cookie cookie = new Cookie(COOKIE_NAME, "value-for-" + mode);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", mode);

        if ("None".equals(mode)) {
            cookie.setSecure(true);
        }

        response.addCookie(cookie);

        response.getWriter().println("Set-Cookie: " + COOKIE_NAME + "=value-for-" + mode
                + "; Path=/; HttpOnly" + ("None".equals(mode) ? "; Secure" : "")
                + "; SameSite=" + mode);
        response.getWriter().println();
        response.getWriter().println("Browsers now default to SameSite=Lax when the attribute is absent.");
        response.getWriter().println("Spring Boot equivalent:");
        response.getWriter().println("  server.servlet.session.cookie.same-site=strict");
    }

    private boolean isValidMode(String mode) {
        return "Strict".equals(mode) || "Lax".equals(mode) || "None".equals(mode);
    }
}
