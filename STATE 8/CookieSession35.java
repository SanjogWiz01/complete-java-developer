/*
 * CookieSession35.java
 *
 * Topic: Server-side sessions versus signed tokens for APIs.
 *
 * A session keeps state on the server, so a token is easy to revoke but
 * every request needs a store lookup. A signed token is self-contained, so
 * any instance can validate it, but it stays valid until it expires unless
 * you add a server-side deny list.
 *
 * The token below is a compact HS256 JWT built by hand to show the three
 * parts. Real projects should use a vetted library such as jjwt.
 */
package com.example.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@WebServlet("/api/auth")
public class CookieSession35 extends HttpServlet {

    private static final String SECRET = "load-this-from-configuration-not-source";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_COOKIE = "apiToken";
    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(30);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        if ("true".equals(request.getParameter("issue"))) {
            String token = issue("sanjog");
            Cookie cookie = new Cookie(TOKEN_COOKIE, token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge((int) TOKEN_LIFETIME.toSeconds());
            cookie.setAttribute("SameSite", "Strict");
            response.addCookie(cookie);
            response.getWriter().println("Signed token issued, valid for "
                    + TOKEN_LIFETIME.toMinutes() + " minutes.");
        }

        String token = readToken(request);
        response.getWriter().println();
        response.getWriter().println("token present = " + (token != null));
        response.getWriter().println("signature ok  = " + (token != null && signatureValid(token)));
        response.getWriter().println("signature note: this proves integrity, not that it is unexpired");
    }

    private String issue(String subject) {
        long now = Instant.now().getEpochSecond();
        String header = base64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64("{\"sub\":\"" + subject + "\",\"iat\":" + now
                + ",\"exp\":" + (now + TOKEN_LIFETIME.toSeconds()) + "}");
        String signingInput = header + "." + payload;
        return signingInput + "." + base64(hmac(signingInput));
    }

    private boolean signatureValid(String token) {
        int lastDot = token.lastIndexOf('.');
        if (lastDot < 1) {
            return false;
        }
        String signingInput = token.substring(0, lastDot);
        String expected = base64(hmac(signingInput));
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                token.substring(lastDot + 1).getBytes(StandardCharsets.UTF_8));
    }

    private byte[] hmac(String input) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HMAC-SHA256 is unavailable", e);
        }
    }

    private String base64(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String readToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
