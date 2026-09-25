/*
 * CookieSession34.java
 *
 * Topic: Sharing sessions across a cluster.
 *
 * A container session lives in one JVM's memory, so a second instance cannot
 * see it. Two common fixes are sticky sessions and a shared session store.
 *
 * A shared store is shown here: the application owns the session id cookie
 * and reads and writes attributes through a SessionStore. Swapping the
 * in-memory store for Redis or a database is the only change required.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/session/cluster")
public class CookieSession34 extends HttpServlet {

    interface SessionStore {
        Map<String, String> read(String id);

        void write(String id, Map<String, String> attributes);

        void remove(String id);
    }

    static final class InMemorySessionStore implements SessionStore {

        private final Map<String, Map<String, String>> data = new ConcurrentHashMap<>();

        @Override
        public Map<String, String> read(String id) {
            Map<String, String> attributes = data.get(id);
            return attributes == null ? new HashMap<>() : new HashMap<>(attributes);
        }

        @Override
        public void write(String id, Map<String, String> attributes) {
            attributes.put("lastSeen", Instant.now().toString());
            data.put(id, new HashMap<>(attributes));
        }

        @Override
        public void remove(String id) {
            data.remove(id);
        }
    }

    private static final String SID_COOKIE = "CLUSTER_SID";
    private static final SessionStore STORE = new InMemorySessionStore();
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        String id = readSid(request);
        Map<String, String> attributes;

        if (id == null) {
            id = newSid();
            attributes = new HashMap<>();
            attributes.put("createdAt", Instant.now().toString());

            Cookie cookie = new Cookie(SID_COOKIE, id);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setAttribute("SameSite", "Lax");
            response.addCookie(cookie);
        } else {
            attributes = STORE.read(id);
        }

        if ("true".equals(request.getParameter("logout"))) {
            STORE.remove(id);
            Cookie dead = new Cookie(SID_COOKIE, "");
            dead.setMaxAge(0);
            dead.setPath("/");
            response.addCookie(dead);
            response.getWriter().println("Session removed from the shared store.");
            return;
        }

        Integer hits = Integer.valueOf(attributes.getOrDefault("hits", "0"));
        attributes.put("hits", String.valueOf(hits + 1));
        attributes.put("username", "sanjog");
        STORE.write(id, attributes);

        response.getWriter().println("session id  = " + id);
        response.getWriter().println("hits        = " + attributes.get("hits"));
        response.getWriter().println("username    = " + attributes.get("username"));
        response.getWriter().println("lastSeen    = " + attributes.get("lastSeen"));
        response.getWriter().println();
        response.getWriter().println("Call /session/cluster?logout=true to drop it.");
    }

    private String newSid() {
        byte[] bytes = new byte[24];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String readSid(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (SID_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private String fingerprint(String id) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(id.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return UUID.nameUUIDFromBytes(hash).toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }
}
