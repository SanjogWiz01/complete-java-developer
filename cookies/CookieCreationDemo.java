import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cookie Creation Demo (Java 8)
 * ------------------------------
 * Shows how an HTTP cookie is created from scratch:
 *   - name=value pair
 *   - attributes: Expires, Max-Age, Path, Domain, Secure, HttpOnly, SameSite
 * and how the final "Set-Cookie" header string is built.
 *
 * Run:  java CookieCreationDemo
 */
public class CookieCreationDemo {

    private static final DateTimeFormatter HTTP_DATE =
            DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);

    /** Simple cookie model built by hand (no servlet API needed). */
    static class HttpCookieBuilder {
        private final String name;
        private String value;
        private String path = "/";
        private String domain;
        private long maxAgeSeconds = -1;          // -1 = session cookie
        private boolean secure;
        private boolean httpOnly;
        private String sameSite;

        HttpCookieBuilder(String name, String value) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Cookie name must not be empty");
            }
            if (name.chars().anyMatch(c -> Character.isWhitespace(c) || c == ';' || c == ',')) {
                throw new IllegalArgumentException("Illegal character in cookie name: " + name);
            }
            this.name = name;
            setValue(value);
        }

        HttpCookieBuilder setValue(String value) {
            if (value != null && value.chars().anyMatch(c -> c == ';' || c == ',' || c == '\r' || c == '\n')) {
                throw new IllegalArgumentException("Illegal character in cookie value: " + value);
            }
            this.value = value == null ? "" : value;
            return this;
        }

        HttpCookieBuilder path(String path)        { this.path = path; return this; }
        HttpCookieBuilder domain(String domain)    { this.domain = domain; return this; }
        HttpCookieBuilder maxAge(long seconds)     { this.maxAgeSeconds = seconds; return this; }
        HttpCookieBuilder secure()                 { this.secure = true; return this; }
        HttpCookieBuilder httpOnly()               { this.httpOnly = true; return this; }
        HttpCookieBuilder sameSite(String policy)  { this.sameSite = policy; return this; }

        /** Produces the exact text a server sends in the "Set-Cookie:" response header. */
        String toSetCookieHeader() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append('=').append(value);
            if (domain != null)      sb.append("; Domain=").append(domain);
            if (path != null)        sb.append("; Path=").append(path);
            if (maxAgeSeconds >= 0) {
                sb.append("; Max-Age=").append(maxAgeSeconds);
                Instant expiresAt = Instant.now().plusSeconds(maxAgeSeconds);
                sb.append("; Expires=").append(HTTP_DATE.format(expiresAt));
            }
            if (secure)              sb.append("; Secure");
            if (httpOnly)            sb.append("; HttpOnly");
            if (sameSite != null)    sb.append("; SameSite=").append(sameSite);
            return sb.toString();
        }

        @Override
        public String toString() { return toSetCookieHeader(); }
    }

    public static void main(String[] args) {
        Map<String, String> examples = new LinkedHashMap<>();

        // 1. The simplest possible cookie - lives only for this browser session.
        HttpCookieBuilder sessionCookie = new HttpCookieBuilder("sessionId", "abc123xyz");
        examples.put("Session cookie (deleted when browser closes)", sessionCookie.toSetCookieHeader());

        // 2. Persistent cookie - valid for 7 days.
        HttpCookieBuilder rememberMe = new HttpCookieBuilder("rememberMe", "sanjog")
                .maxAge(7 * 24 * 60 * 60)
                .httpOnly()
                .sameSite("Lax");
        examples.put("Persistent cookie (7 days)", rememberMe.toSetCookieHeader());

        // 3. Scoped to one path, secure only over HTTPS.
        HttpCookieBuilder cartCookie = new HttpCookieBuilder("cart", "item=42;qty=1".replace(';', '%'))
                .path("/shop")
                .maxAge(30 * 60)
                .secure();
        examples.put("Path-scoped secure cookie (30 min)", cartCookie.toSetCookieHeader());

        // 4. Language preference shared across sub-domains.
        HttpCookieBuilder langCookie = new HttpCookieBuilder("lang", "np")
                .domain(".example.com")
                .maxAge(365L * 24 * 60 * 60);
        examples.put("Domain-wide cookie (1 year)", langCookie.toSetCookieHeader());

        System.out.println("=== Created Set-Cookie headers ===");
        examples.forEach((desc, header) -> System.out.printf("%-42s -> %s%n", desc, "Set-Cookie: " + header));

        // 5. Expiry timestamp that accompanies a Max-Age of 3600s.
        System.out.println();
        System.out.println("Expires value for a 1-hour cookie: "
                + HTTP_DATE.format(Instant.now().plusSeconds(3600)));

        // 6. Deleting a cookie = Max-Age 0 (or an already passed Expires date).
        HttpCookieBuilder deleted = new HttpCookieBuilder("rememberMe", "").maxAge(0);
        System.out.println("Delete existing cookie           -> Set-Cookie: " + deleted);

        // 7. Parsing the "Cookie:" request header a client sends back.
        String browserHeader = "sessionId=abc123xyz; lang=np; cart=item%3D42";
        System.out.println();
        System.out.println("=== Parsing client request header ===");
        System.out.println("Cookie: " + browserHeader);
        parseCookieHeader(browserHeader)
                .forEach((k, v) -> System.out.printf("   %-10s = %s%n", k, v));
    }

    /** Splits "a=1; b=2" into an ordered map - mirrors what containers do internally. */
    static Map<String, String> parseCookieHeader(String header) {
        Map<String, String> cookies = new LinkedHashMap<>();
        if (header == null || header.isEmpty()) {
            return cookies;
        }
        for (String part : header.split(";")) {
            String pair = part.trim();
            int eq = pair.indexOf('=');
            if (eq > 0) {
                cookies.put(pair.substring(0, eq).trim(), pair.substring(eq + 1).trim());
            } else if (!pair.isEmpty()) {
                cookies.put(pair, "");                       // flag-style cookie
            }
        }
        return cookies;
    }
}
