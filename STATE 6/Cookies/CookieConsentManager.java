import java.time.Instant;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cookie Consent Manager (Java 8)
 * -------------------------------
 * GDPR/ePrivacy: a site may only drop non-essential cookies AFTER the user
 * consents. Cookies fall into categories:
 *
 *   NECESSARY   - session id, CSRF token, consent record itself (always allowed)
 *   PREFERENCES - language, theme
 *   ANALYTICS   - usage statistics
 *   MARKETING   - ad tracking
 *
 * This manager decides, per category, whether a Set-Cookie may be sent,
 * and records who agreed to what and when.
 *
 * Run:  java CookieConsentManager
 */
public class CookieConsentManager {

    public enum Category { NECESSARY, PREFERENCES, ANALYTICS, MARKETING }

    static final class Consent {
        final String userId;
        final Set<Category> granted;
        final Instant decidedAt;

        Consent(String userId, Set<Category> granted) {
            this.userId = userId;
            this.granted = EnumSet.copyOf(granted);
            this.decidedAt = Instant.now();
        }
    }

    private final Map<String, Consent> consents = new LinkedHashMap<>();

    /** User clicked "Accept all" / "Only necessary" / a custom mix. */
    public void record(String userId, Category... allowed) {
        EnumSet<Category> set = EnumSet.of(Category.NECESSARY);
        for (Category c : allowed) {
            if (c != Category.NECESSARY) {
                set.add(c);
            }
        }
        consents.put(userId, new Consent(userId, set));
    }

    public boolean maySetCookie(String userId, Category category) {
        Consent consent = consents.get(userId);
        if (category == Category.NECESSARY) {
            return true;                       // strictly necessary needs no consent
        }
        return consent != null && consent.granted.contains(category);
    }

    /** Simulates the server deciding which Set-Cookie headers to send. */
    public void applyCookies(String userId, Map<String, Category> wantedCookies) {
        System.out.println("Cookie banner decision for '" + userId + "':");
        wantedCookies.forEach((cookieName, category) -> {
            boolean allowed = maySetCookie(userId, category);
            System.out.println("  " + (allowed ? "SET  " : "BLOCK") + "  " + cookieName
                    + "  (" + category + ")");
        });
    }

    public void printAuditTrail() {
        System.out.println("Audit trail:");
        consents.forEach((user, c) ->
                System.out.println("  " + user + " @ " + c.decidedAt + " -> " + c.granted));
    }

    public static void main(String[] args) {
        CookieConsentManager site = new CookieConsentManager();

        Map<String, Category> cookiesWanted = new LinkedHashMap<>();
        cookiesWanted.put("JSESSIONID", Category.NECESSARY);
        cookiesWanted.put("theme", Category.PREFERENCES);
        cookiesWanted.put("_ga", Category.ANALYTICS);
        cookiesWanted.put("_fbp", Category.MARKETING);

        site.record("anon-1");
        site.applyCookies("anon-1", cookiesWanted);

        System.out.println();
        site.record("user-42", Category.PREFERENCES, Category.ANALYTICS);
        site.applyCookies("user-42", cookiesWanted);

        System.out.println();
        site.record("user-99", Category.values());
        site.applyCookies("user-99", cookiesWanted);

        System.out.println();
        site.printAuditTrail();

        System.out.println();
        System.out.println("Unknown visitor wants analytics cookie? "
                + site.maySetCookie("nobody", Category.ANALYTICS));
    }
}
