import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Client-side Cookie Jar (Java 8)
 * ------------------------------
 * Browsers are the real "cookie servers": they receive Set-Cookie headers,
 * apply scope rules (Domain, Path, Expires) and attach a "Cookie:" header
 * to matching requests.
 *
 * This class models that jar:
 *
 *   - store(host, path, setCookieHeader)  -> remember a cookie
 *   - cookieHeaderFor(host, path)         -> value for the request header
 *   - expired cookies disappear automatically
 *
 * Run:  java CookieJar   (self-test)
 */
public class CookieJar {

    /** One stored cookie with its scope and lifetime. */
    static final class StoredCookie {
        final String name;
        String value;
        String domain;          // host the cookie belongs to (no leading dot)
        String path;            // default "/" like RFC 6265
        Instant expiresAt;      // null = session cookie

        StoredCookie(String name, String value, String domain, String path, Instant expiresAt) {
            this.name = name;
            this.value = value;
            this.domain = domain == null ? "" : domain.toLowerCase();
            this.path = path == null || path.isEmpty() ? "/" : path;
            this.expiresAt = expiresAt;
        }

        boolean isExpired(Instant now) {
            return expiresAt != null && now.isAfter(expiresAt);
        }

        boolean matches(String host, String path) {
            if (!host.equalsIgnoreCase(domain)) {
                return false;
            }
            return path.startsWith(this.path);
        }
    }

    private final Map<String, StoredCookie> jar = new ConcurrentHashMap<>();

    /**
     * Feeds one "Set-Cookie: name=value; Path=/; Max-Age=3600" header into the jar.
     * Returns true if a cookie was stored or updated.
     */
    public boolean store(String host, String setCookieHeader) {
        String[] parts = setCookieHeader.split(";", 2);
        String nameValue = parts[0].trim();
        int eq = nameValue.indexOf('=');
        if (eq <= 0) {
            return false;
        }
        String name = nameValue.substring(0, eq).trim();
        String value = nameValue.substring(eq + 1).trim();

        String domain = host.toLowerCase();
        String path = "/";
        Instant expiresAt = null;

        if (parts.length == 2) {
            for (String attr : parts[1].split(";")) {
                String[] kv = attr.split("=", 2);
                String key = kv[0].trim().toLowerCase();
                String val = kv.length == 2 ? kv[1].trim() : "";
                switch (key) {
                    case "domain":
                        domain = val.startsWith(".") ? val.substring(1).toLowerCase() : val.toLowerCase();
                        break;
                    case "path":
                        path = val;
                        break;
                    case "expires":
                        expiresAt = parseHttpDate(val);
                        break;
                    case "max-age":
                        try {
                            long seconds = Long.parseLong(val);
                            expiresAt = seconds <= 0 ? Instant.EPOCH : Instant.now().plusSeconds(seconds);
                        } catch (NumberFormatException ignored) { }
                        break;
                    default:
                        break;
                }
            }
        }

        StoredCookie cookie = new StoredCookie(name, value, domain, path, expiresAt);
        if (cookie.isExpired(Instant.now())) {
            jar.remove(key(name, domain, path));
            return false;
        }
        jar.put(key(name, domain, path), cookie);
        return true;
    }

    /** Builds the "Cookie:" header value for a request to host+path, or null. */
    public String cookieHeaderFor(String host, String path) {
        Instant now = Instant.now();
        Map<String, String> selected = new TreeMap<>();
        for (StoredCookie c : jar.values()) {
            if (c.isExpired(now)) {
                jar.remove(key(c.name, c.domain, c.path));
            } else if (c.matches(host, path)) {
                selected.put(c.name, c.value);
            }
        }
        if (selected.isEmpty()) {
            return null;
        }
        StringJoiner joiner = new StringJoiner("; ");
        selected.forEach((n, v) -> joiner.add(n + "=" + v));
        return joiner.toString();
    }

    public void clear() {
        jar.clear();
    }

    private static String key(String name, String domain, String path) {
        return name + "|" + domain + "|" + path;
    }

    private static final java.time.format.DateTimeFormatter HTTP_DATE =
            java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.withZone(java.time.ZoneOffset.UTC);

    private static Instant parseHttpDate(String text) {
        try {
            return Instant.from(HTTP_DATE.parse(text));
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        CookieJar browser = new CookieJar();

        System.out.println("> GET https://shop.example.com/login");
        browser.store("shop.example.com", "session=abc123; Path=/; HttpOnly");
        browser.store("shop.example.com", "theme=dark; Path=/; Max-Age=86400");

        System.out.println("> GET https://shop.example.com/cart");
        System.out.println("  Cookie: " + browser.cookieHeaderFor("shop.example.com", "/cart"));

        System.out.println("> DELETE session cookie via Max-Age=0");
        browser.store("shop.example.com", "session=x; Path=/; Max-Age=0");
        System.out.println("  Cookie: " + browser.cookieHeaderFor("shop.example.com", "/cart"));

        System.out.println("> other.com must not see example's cookies");
        String foreign = browser.cookieHeaderFor("other.com", "/");
        System.out.println("  Cookie: " + (foreign == null ? "(none)" : foreign));
    }
}
