/*
 * 06_Spring_Boot_Cookie_Session_Controller.java
 *
 * Spring Boot MVC examples for cookies and HttpSession.
 *
 * Dependencies:
 * - spring-boot-starter-web
 */
package com.example.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/state")
public class CookieSessionController {

    @GetMapping("/cookie")
    public String createCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("theme", "dark");
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // enable in HTTPS production
        response.addCookie(cookie);

        return "Cookie created";
    }

    @GetMapping("/cookie/read")
    public String readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return "No cookies";
        }

        for (Cookie cookie : cookies) {
            if ("theme".equals(cookie.getName())) {
                return "theme=" + cookie.getValue();
            }
        }

        return "theme cookie not found";
    }

    @GetMapping("/session")
    public String session(HttpSession session) {
        Integer count = (Integer) session.getAttribute("count");

        if (count == null) {
            count = 0;
        }

        session.setAttribute("count", ++count);
        return "Session count=" + count + ", id=" + session.getId();
    }

    @PostMapping("/session/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "Session invalidated";
    }
}
