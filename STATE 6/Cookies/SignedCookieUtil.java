import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Signed Cookie Utility (Java 8)
 * ------------------------------
 * A browser can read AND edit its cookies, so a plain "user=sanjog" cookie
 * is trivially forgeable. The fix: append an HMAC-SHA256 signature of the
 * value. The server can verify the signature but a client cannot forge it
 * without the secret key.
 *
 *   value          = base64url(payload) + "." + base64url(hmac(payload))
 *
 * This is the same idea behind JWTs and Rails' signed cookies.
 *
 * Run:  java SignedCookieUtil   (self-test)
 */
public final class SignedCookieUtil {

    private static final String SEPARATOR = ".";

    private final byte[] secret;

    public SignedCookieUtil(String secret) {
        if (secret == null || secret.length() < 16) {
            throw new IllegalArgumentException("Secret must be at least 16 characters");
        }
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    /** Produces the value to store in the Set-Cookie header: payload.signature */
    public String sign(String payload) {
        String encoded = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return encoded + SEPARATOR + encode(hmac(encoded));
    }

    /** Returns the original payload if - and only if - the signature matches. */
    public java.util.Optional<String> verify(String signedValue) {
        int dot = signedValue.lastIndexOf(SEPARATOR);
        if (dot <= 0 || dot == signedValue.length() - 1) {
            return java.util.Optional.empty();
        }
        String encoded = signedValue.substring(0, dot);
        String givenSignature = signedValue.substring(dot + 1);
        byte[] expected = hmac(encoded);
        byte[] given = decode(givenSignature);
        if (given == null || !java.security.MessageDigest.isEqual(expected, given)) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(new String(
                Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8));
    }

    private byte[] hmac(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            return mac.doFinal(data.getBytes(StandardCharsets.US_ASCII));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC unavailable", e);
        }
    }

    private static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static byte[] decode(String text) {
        try {
            return Base64.getUrlDecoder().decode(text);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Attacker decodes the payload, edits it, keeps the old signature. */
    private static String forge(String signedValue) {
        int dot = signedValue.lastIndexOf(SEPARATOR);
        String payload = new String(Base64.getUrlDecoder().decode(signedValue.substring(0, dot)),
                StandardCharsets.UTF_8);
        String evilPayload = payload.replace("user=sanjog", "user=admin");
        String reEncoded = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(evilPayload.getBytes(StandardCharsets.UTF_8));
        return reEncoded + signedValue.substring(dot);
    }

    public static void main(String[] args) {
        SignedCookieUtil util = new SignedCookieUtil("super-secret-key-123456");

        String themeCookie = util.sign("theme=dark;user=sanjog");
        System.out.println("Set-Cookie: prefs=" + themeCookie + "; Path=/; HttpOnly; SameSite=Lax");
        System.out.println("Verified  : " + util.verify(themeCookie).orElse("<rejected>"));

        String tampered = forge(themeCookie);
        System.out.println("Tampered  : " + tampered);
        System.out.println("Verify    : " + util.verify(tampered).orElse("<rejected - signature mismatch>"));

        System.out.println("Garbage   : " + util.verify("not-a-cookie").orElse("<rejected>"));
    }
}
