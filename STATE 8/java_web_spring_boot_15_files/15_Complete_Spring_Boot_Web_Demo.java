/*
 * 15_Complete_Spring_Boot_Web_Demo.java
 *
 * FINAL INTEGRATED EXAMPLE
 *
 * Combines:
 * - Spring Boot REST
 * - Cookie
 * - HttpSession
 * - Service layer
 * - Strategy pattern
 * - Security/session concepts
 *
 * Suggested Maven dependency:
 *
 * spring-boot-starter-web
 *
 * For security:
 * spring-boot-starter-security
 *
 * Main application class would normally be in its own Java file.
 */
package com.example.demo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class CompleteWebDemoController {

    private final DemoService service;

    public CompleteWebDemoController(DemoService service) {
        this.service = service;
    }

    // ---------------- COOKIE ----------------

    @PostMapping("/cookie")
    public String setCookie(HttpServletResponse response) {

        Cookie preference = new Cookie("language", "en");
        preference.setMaxAge(7 * 24 * 60 * 60);
        preference.setPath("/");
        preference.setHttpOnly(true);

        // Use Secure=true in production HTTPS.
        response.addCookie(preference);

        return "Preference cookie created";
    }

    @GetMapping("/cookie")
    public String getCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return "No cookies";
        }

        for (Cookie cookie : cookies) {
            if ("language".equals(cookie.getName())) {
                return "language=" + cookie.getValue();
            }
        }

        return "language cookie not found";
    }

    // ---------------- SESSION ----------------

    @PostMapping("/login/{username}")
    public String login(@PathVariable String username, HttpSession session) {

        session.setAttribute("username", username);
        return "Logged in as " + username
                + ", session=" + session.getId();
    }

    @GetMapping("/profile")
    public String profile(HttpSession session) {

        Object username = session.getAttribute("username");

        if (username == null) {
            return "Not logged in";
        }

        return "Welcome " + username;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();
        return "Logged out";
    }

    // ---------------- SERVICE ----------------

    @GetMapping("/price")
    public double price(@RequestParam double amount,
                         @RequestParam(defaultValue = "regular") String type) {

        return service.calculate(amount, type);
    }
}

interface PriceStrategy {
    double calculate(double amount);
}

@org.springframework.stereotype.Component("regular")
class RegularPriceStrategy implements PriceStrategy {
    public double calculate(double amount) {
        return amount;
    }
}

@org.springframework.stereotype.Component("student")
class StudentPriceStrategy implements PriceStrategy {
    public double calculate(double amount) {
        return amount * 0.90;
    }
}

@Service
class DemoService {

    private final List<PriceStrategy> strategies;

    DemoService(List<PriceStrategy> strategies) {
        this.strategies = strategies;
    }

    double calculate(double amount, String type) {
        return strategies.stream()
                .filter(strategy -> strategy.getClass()
                        .getSimpleName()
                        .toLowerCase()
                        .startsWith(type.toLowerCase()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown strategy: " + type))
                .calculate(amount);
    }
}

/*
 * EXAM / INTERVIEW SUMMARY:
 *
 * Cookie:
 *   Client-side state, sent by browser with matching requests.
 *
 * Session:
 *   Server-side state identified by a session ID, commonly transported
 *   through a cookie.
 *
 * Spring Boot:
 *   Simplifies Spring application configuration and deployment.
 *
 * Design patterns:
 *   Creational -> object creation
 *   Structural -> object/class composition
 *   Behavioral -> communication and responsibilities
 *
 * Spring commonly demonstrates:
 *   Singleton, Factory, Proxy, Strategy, Template Method, Observer-style
 *   event mechanisms, Dependency Injection and IoC.
 */
