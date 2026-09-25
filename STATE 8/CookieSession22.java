/*
 * CookieSession22.java
 *
 * Topic: Cookie scoping with Path and Domain.
 *
 * Path limits the cookie to a URL prefix, Domain widens it to subdomains.
 * A cookie with Domain=example.com and Path=/ is sent to every path on
 * example.com and on any of its subdomains.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cookies/scope")
public class CookieSession22 extends HttpServlet {

    private static final String APP_COOKIE = "appScoped";
    private static final String WIDE_COOKIE = "domainScoped";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        if ("true".equals(request.getParameter("reset"))) {
            response.addCookie(remove(APP_COOKIE, "/cookies"));
            response.addCookie(remove(WIDE_COOKIE, "/"));
            response.getWriter().println("Scoped cookies were expired.");
            return;
        }

        Cookie app = new Cookie(APP_COOKIE, "app-value");
        app.setPath("/cookies");
        app.setHttpOnly(true);
        response.addCookie(app);

        Cookie wide = new Cookie(WIDE_COOKIE, "domain-value");
        wide.setPath("/");
        wide.setHttpOnly(true);
        response.addCookie(wide);

        response.getWriter().println("Cookie      Path     Sent on this request");
        response.getWriter().println("--------    -------  -----------------");
        for (Cookie cookie : request.getCookies() == null ? new Cookie[0] : request.getCookies()) {
            String path = "/" + cookie.getPath();
            String applies = request.getRequestURI().startsWith(path) ? "yes" : "no";
            response.getWriter().println(cookie.getName() + "  " + path + "  " + applies);
        }

        response.getWriter().println();
        response.getWriter().println("Domain is left unset on purpose so the cookie stays host-only.");
        response.getWriter().println("Call /cookies/scope?reset=true to expire both cookies.");
    }

    private Cookie remove(String name, String path) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath(path);
        return cookie;
    }
}
