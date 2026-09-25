/*
 * CookieSession31.java
 *
 * Topic: Cookie integrity with an HMAC signature.
 *
 * A cookie is attacker-controlled input. Signing the value with
 * HMAC-SHA256 lets the server detect any modification before trusting it.
 *
 * This is integrity only, not confidentiality. If the payload must also be
 * unreadable by the client, encrypt it as well.
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
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@WebServlet("/cookies/integrity")
public class CookieSession31 extends HttpServlet {

    private static final String COOKIE_NAME = "signedValue";
    private static final String SECRET = "load-this-from-configuration-not-source";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");

        if ("true".equals(request.getParameter("sign"))) {
            String value = request.getParameter("value");
            if (value == null) {
                value = "role=admin";
            }
            Cookie cookie = new Cookie(COOKIE_NAME, sign(value));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);
            response.getWriter().println("Signed cookie sent.");
            return;
        }

        String raw = read(request);
        if (raw == null) {
            response.getWriter().println("No cookie yet. Try /cookies/integrity?sign=true");
            return;
        }

        if (verify(raw)) {
            response.getWriter().println("Signature valid. Payload = " + payloadOf(raw));
        } else {
            response.getWriter().println("Signature INVALID. Cookie was tampered with.");
        }
    }

    private String sign(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return value + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(bytes));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HMAC-SHA256 is unavailable", e);
        }
    }

    private boolean verify(String signed) {
        int separator = signed.lastIndexOf('.');
        if (separator < 1) {
            return false;
        }
        String expected = sign(signed.substring(0, separator));
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                signed.getBytes(StandardCharsets.UTF_8));
    }

    private String payloadOf(String signed) {
        int separator = signed.lastIndexOf('.');
        return separator < 1 ? "" : signed.substring(0, separator);
    }

    private String read(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
