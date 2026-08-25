import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CSRF Token Manager (Java 8)
 * ---------------------------
 * Cross-Site Request Forgery: evil.com embeds
 *     <form action="https://bank.example/transfer" method="POST">
 * and the browser helpfully attaches the victim's session cookie.
 *
 * Two standard defenses implemented here:
 *
 *   1. Synchronizer token  - random token stored in the SESSION, embedded in
 *      every form; POSTs must echo it back.
 *   2. Double-submit cookie- token stored in a (readable) cookie AND sent as
 *      a request field/headers; server only checks they match, because
 *      attacker sites cannot read or set our cookies.
 *
 * Run:  java CsrfTokenManager   (self-test)
 */
public class CsrfTokenManager {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> sessionTokens = new ConcurrentHashMap<>();

    /** 1. Called when rendering a form for a given session. */
    public String tokenFor(String sessionId) {
        return sessionTokens.computeIfAbsent(sessionId, k -> newToken());
    }

    /** 1. Validates the hidden form field against the stored session token. */
    public boolean validateSynchronizer(String sessionId, String submitted) {
        String expected = sessionTokens.get(sessionId);
        if (expected == null || submitted == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                submitted.getBytes(StandardCharsets.UTF_8));
    }

    /** 2. The cookie value the client also echoes in the "_csrf" field. */
    public String issueDoubleSubmitCookie() {
        return newToken();
    }

    /** 2. Cookie value and form field must be byte-for-byte equal. */
    public boolean validateDoubleSubmit(String cookieValue, String formField) {
        if (cookieValue == null || formField == null || cookieValue.isEmpty()) {
            return false;
        }
        return MessageDigest.isEqual(
                cookieValue.getBytes(StandardCharsets.UTF_8),
                formField.getBytes(StandardCharsets.UTF_8));
    }

    public void invalidate(String sessionId) {
        sessionTokens.remove(sessionId);
    }

    private String newToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(Character.forDigit((b >> 4) & 0xF, 16));
            hex.append(Character.forDigit(b & 0xF, 16));
        }
        return hex.toString();
    }

    public static void main(String[] args) {
        CsrfTokenManager app = new CsrfTokenManager();
        String session = "S-100";

        String token = app.tokenFor(session);
        System.out.println("<input type=hidden name=_csrf value=" + token.substring(0, 12) + "...>");

        System.out.println("Honest POST, matching token : "
                + app.validateSynchronizer(session, token));
        System.out.println("Forged POST, missing token  : "
                + app.validateSynchronizer(session, null));
        System.out.println("Replayed old token          : "
                + app.validateSynchronizer(session, token));
        app.invalidate(session);
        System.out.println("After logout/re-login       : "
                + app.validateSynchronizer(session, token));

        String cookieToken = app.issueDoubleSubmitCookie();
        System.out.println();
        System.out.println("Double-submit ok            : "
                + app.validateDoubleSubmit(cookieToken, cookieToken));
        System.out.println("Double-submit tampered      : "
                + app.validateDoubleSubmit(cookieToken, "deadbeef"));
    }
}
