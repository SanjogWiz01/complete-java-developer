import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

/**
 * Secure Session ID Generator (Java 8)
 * ------------------------------------
 * The session ID is the ONLY thing standing between an attacker and
 * someone else's session, so it must be:
 *
 *   - unpredictable  -> java.security.SecureRandom (never Math.random()!)
 *   - long enough    -> at least 128 bits of entropy (16 bytes)
 *   - opaque         -> no usernames, timestamps or counters inside
 *   - unique         -> collision probability ~ 2^-128
 *
 * This class shows how to build such IDs and why weaker sources fail.
 *
 * Run:  java SessionIdGenerator
 */
public class SessionIdGenerator {

    private static final int MIN_BYTES = 16;   // 128-bit floor recommended by OWASP

    private final SecureRandom random;
    private final int idBytes;

    public SessionIdGenerator(int idBytes) {
        if (idBytes < MIN_BYTES) {
            throw new IllegalArgumentException("Use at least " + MIN_BYTES + " bytes (" + MIN_BYTES * 8 + " bits)");
        }
        this.idBytes = idBytes;
        this.random = new SecureRandom();
    }

    public SessionIdGenerator() {
        this(MIN_BYTES * 2);                   // default: 256-bit ids
    }

    /** URL-safe, no padding: safe inside a cookie value. */
    public String nextId() {
        byte[] bytes = new byte[idBytes];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Demonstrates how quickly weak ids repeat and why that is dangerous. */
    private static void weakIdComparison() {
        Set<String> seen = new HashSet<>();
        int collisions = 0;
        for (int i = 0; i < 100_000; i++) {
            String weak = String.valueOf((int) (Math.random() * 10_000));   // predictable + tiny space
            if (!seen.add(weak)) {
                collisions++;
            }
        }
        System.out.println("Math.random()*10000 : " + collisions + " collisions in 100k ids");
    }

    public static void main(String[] args) {
        SessionIdGenerator generator = new SessionIdGenerator();

        System.out.println("Secure session ids (256-bit):");
        for (int i = 1; i <= 5; i++) {
            System.out.println("  " + i + ": JSESSIONID=" + generator.nextId());
        }
        System.out.println("Length: " + generator.nextId().length()
                + " chars for " + (generator.idBytes * 8) + " bits of entropy");

        weakIdComparison();

        try {
            new SessionIdGenerator(4);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected short key: " + e.getMessage());
        }
    }
}
