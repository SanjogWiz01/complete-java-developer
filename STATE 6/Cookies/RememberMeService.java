import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Remember-Me Cookie Service (Java 8)
 * -----------------------------------
 * "Remember me" must NEVER store the plain password (or even a reusable
 * password hash) in the cookie. Safer pattern:
 *
 *   - cookie value = series:token   (both random, token rotates on every use)
 *   - server keeps only SHA-256 hashes + username + expiry
 *   - stolen cookie works once; next login detects theft and kills the series
 *   - a database leak reveals nothing usable (only hashes are stored)
 *
 * Run:  java RememberMeService
 */
public class RememberMeService {

    private static final long VALID_DAYS = 30;

    static final class StoredToken {
        final String user;
        final String tokenHash;
        final Instant expiresAt;

        StoredToken(String user, String tokenHash) {
            this.user = user;
            this.tokenHash = tokenHash;
            this.expiresAt = Instant.now().plusSeconds(VALID_DAYS * 24L * 3600L);
        }
    }

    private final SecureRandom random = new SecureRandom();
    private final Map<String, StoredToken> store = new LinkedHashMap<>();

    /** Login with "remember me" ticked -> returns cookie value "series:token". */
    public String issueCookie(String user) {
        String series = randomString();
        String token = randomString();
        store.put(series, new StoredToken(user, sha256(token)));
        return series + ":" + token;
    }

    /**
     * Auto-login from the cookie.
     * OK      -> same user may log in again, but with a NEW token.
     * STOLEN  -> valid series + stale token means someone replayed it first.
     */
    public Optional<String> autoLogin(String cookieValue) {
        int colon = cookieValue.indexOf(':');
        if (colon <= 0 || colon == cookieValue.length() - 1) {
            return Optional.empty();
        }
        String series = cookieValue.substring(0, colon);
        String token = cookieValue.substring(colon + 1);
        StoredToken stored = store.get(series);
        if (stored == null || Instant.now().isAfter(stored.expiresAt)) {
            return Optional.empty();
        }
        if (!MessageDigest.isEqual(
                stored.tokenHash.getBytes(StandardCharsets.UTF_8),
                sha256(token).getBytes(StandardCharsets.UTF_8))) {
            store.remove(series);
            System.out.println("  !! token replay detected for series " + shortId(series) + " - series revoked");
            return Optional.empty();
        }
        String freshToken = randomString();
        store.put(series, new StoredToken(stored.user, sha256(freshToken)));
        return Optional.of(stored.user);
    }

    public void revoke(String cookieValue) {
        int colon = cookieValue.indexOf(':');
        if (colon > 0) {
            store.remove(cookieValue.substring(0, colon));
        }
    }

    private String randomString() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String sha256(String text) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                hex.append(Character.forDigit((b >> 4) & 0xF, 16));
                hex.append(Character.forDigit(b & 0xF, 16));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }

    private static String shortId(String s) {
        return s.substring(0, 6);
    }

    public static void main(String[] args) {
        RememberMeService service = new RememberMeService();

        String cookie = service.issueCookie("sanjog");
        System.out.println("Set-Cookie: remember-me=" + shortId(cookie) + "...; Max-Age=2592000; HttpOnly; Secure");

        System.out.println("Auto-login #1: " + service.autoLogin(cookie).orElse("<denied>"));
        System.out.println("Auto-login #2 with SAME cookie (replay):");
        System.out.println("               " + service.autoLogin(cookie).orElse("<denied>"));

        String second = service.issueCookie("priya");
        service.revoke(second);
        System.out.println("Revoked cookie login: " + service.autoLogin(second).orElse("<denied>"));

        System.out.println("Garbage cookie login: " + service.autoLogin("nonsense").orElse("<denied>"));
    }
}
