import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Cookie Implementation Utility (Java 8)
 * --------------------------------------
 * A small, dependency-free cookie toolkit used by CookieServer.java:
 *
 *   - build(...)          : create Set-Cookie header values
 *   - parseRequestHeader(): read the browser's "Cookie:" request header
 *   - encode/decode       : safe values for characters like '=', ';', ' '
 *   - formatExpires()     : RFC 1123 date required by the cookie spec
 *
 * Run:  java CookieImplementation   (self-test)
 */
public final class CookieImplementation {

    private static final DateTimeFormatter HTTP_DATE =
            DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);

    private CookieImplementation() { }

    /* ---------------------------------------------------------------------
     * 1. Building Set-Cookie response headers
     * ------------------------------------------------------------------- */

    /** Creates a session cookie that disappears when the browser closes. */
    public static String sessionCookie(String name, String value) {
        return name + "=" + encode(value);
    }

    /**
     * Full builder-style factory.
     *
     * @param maxAgeSeconds -1 for a session cookie; 0 deletes the cookie
     */
    public static String build(String name,
                               String value,
                               long maxAgeSeconds,
                               String path,
                               boolean httpOnly) {
        StringBuilder sb = new StringBuilder(name).append('=').append(encode(value));
        if (maxAgeSeconds >= 0) {
            sb.append("; Max-Age=").append(maxAgeSeconds)
              .append("; Expires=").append(formatExpires(Instant.now().plusSeconds(maxAgeSeconds)));
        }
        if (path != null && !path.isEmpty()) {
            sb.append("; Path=").append(path);
        } else {
            sb.append("; Path=/");
        }
        if (httpOnly) {
            sb.append("; HttpOnly");
        }
        return sb.toString();
    }

    /** Header value that removes a cookie from the browser. */
    public static String deleteCookie(String name) {
        return build(name, "", 0, "/", true);
    }

    /* ---------------------------------------------------------------------
     * 2. Reading the client's request
     * ------------------------------------------------------------------- */

    /**
     * Parses a raw "Cookie: a=1; b=2" header into an ordered map.
     * Returns an empty map when the header is absent.
     */
    public static Map<String, String> parseRequestHeader(String cookieHeader) {
        if (cookieHeader == null || cookieHeader.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new LinkedHashMap<>();
        for (String part : cookieHeader.split(";")) {
            String pair = part.trim();
            int eq = pair.indexOf('=');
            if (eq > 0) {
                result.put(pair.substring(0, eq).trim(), decode(pair.substring(eq + 1).trim()));
            }
        }
        return result;
    }

    /** Convenience lookup used by request handlers. */
    public static Optional<String> find(Map<String, String> cookies, String name) {
        return Optional.ofNullable(cookies.get(name));
    }

    /* ---------------------------------------------------------------------
     * 3. Helpers
     * ------------------------------------------------------------------- */

    public static String encode(String raw) {
        try {
            return URLEncoder.encode(raw == null ? "" : raw, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);  // cannot happen on the JVM
        }
    }

    public static String decode(String encoded) {
        try {
            return encoded == null ? "" : URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            return "";                                                  // tolerate malformed input
        }
    }

    public static String formatExpires(Instant instant) {
        return HTTP_DATE.format(instant);
    }

    /* ---------------------------------------------------------------------
     * 4. Self-test
     * ------------------------------------------------------------------- */

    public static void main(String[] args) {
        System.out.println("build (session)  : " + sessionCookie("user", "sanjog w"));
        System.out.println("build (1 day)    : " + build("theme", "dark", 86_400, "/", true));
        System.out.println("delete           : " + deleteCookie("theme"));

        Map<String, String> parsed = parseRequestHeader("user=sanjog+w; theme=dark; empty=");
        System.out.println("\nParsed request   : " + parsed);
        System.out.println("find('theme')    : " + find(parsed, "theme").orElse("<absent>"));
        System.out.println("find('nope')     : " + find(parsed, "nope").orElse("<absent>"));

        // Round-trip proof: special characters survive encode -> decode.
        String tricky = "a=b; c d,e";
        String roundTrip = decode(encode(tricky));
        System.out.println("round-trip ok?   : " + roundTrip.equals(tricky));
    }
}
