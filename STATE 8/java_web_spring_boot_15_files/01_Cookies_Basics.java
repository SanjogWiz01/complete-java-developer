/*
 * 01_Cookies_Basics.java
 *
 * Topic: HTTP cookies with Jakarta Servlet.
 * Run inside a servlet container such as embedded Tomcat in Spring Boot.
 *
 * Concepts:
 * - Cookie is client-side state.
 * - Server sends Set-Cookie.
 * - Browser sends Cookie on later requests.
 * - Do not store passwords, session data, or sensitive secrets in cookies.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cookies/basic")
public class CookiesBasicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        Cookie username = new Cookie("username", "Sanjog");
        username.setMaxAge(60 * 60);       // 1 hour
        username.setPath("/");             // available to the whole application
        username.setHttpOnly(true);        // JavaScript cannot read it
        // In production over HTTPS:
        // username.setSecure(true);

        response.addCookie(username);

        response.getWriter().println("Cookie 'username' was created.");
        response.getWriter().println("Refresh the page and inspect request cookies.");
    }
}
