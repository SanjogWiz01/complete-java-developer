import java.util.Arrays;
import java.util.List;

/**
 * Cookie Scope & Attribute Validator (Java 8)
 * -------------------------------------------
 * A cookie's blast radius is decided by its attributes. Misconfigured
 * scopes are a classic security hole:
 *
 *   Domain  - omit it! Setting Domain=example.com also leaks the cookie to
 *             EVERY subdomain (api., evil-cdn., ...).
 *   Path    - narrower paths limit exposure inside one host.
 *   Secure  - cookie may only travel over HTTPS.
 *   HttpOnly- hides the cookie from JavaScript (anti-XSS theft).
 *   SameSite- Lax/Strict blocks cross-site sends (anti-CSRF).
 *   __Host- prefix: browser-enforced "Path=/, Secure, no Domain".
 *
 * This validator flags risky Set-Cookie headers before they ship.
 *
 * Run:  java CookieScopeValidator
 */
public class CookieScopeValidator {

    static final class Finding {
        final String severity;
        final String message;

        Finding(String severity, String message) {
            this.severity = severity;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s", severity, message);
        }
    }

    private static final List<String> RESERVED_SUFFIXES =
            Arrays.asList("com", "org", "net", "gov", "edu", "io", "dev");

    public static void validate(String setCookieHeader, boolean isHttpsResponse) {
        System.out.println("Set-Cookie: " + setCookieHeader);

        String[] parts = setCookieHeader.split(";");
        String nameValue = parts[0].trim();
        if (!nameValue.contains("=") || nameValue.indexOf('=') == 0) {
            System.out.println("  [ERROR] missing name=value pair");
            return;
        }
        String name = nameValue.substring(0, nameValue.indexOf('=')).trim();

        String domain = null;
        String path = "/";
        boolean secure = false;
        boolean httpOnly = false;
        String sameSite = null;

        for (int i = 1; i < parts.length; i++) {
            String[] kv = parts[i].split("=", 2);
            String key = kv[0].trim().toLowerCase();
            String value = kv.length == 2 ? kv[1].trim() : "";
            switch (key) {
                case "domain":   domain = value.toLowerCase(); break;
                case "path":     path = value; break;
                case "secure":   secure = true; break;
                case "httponly": httpOnly = true; break;
                case "samesite": sameSite = value.toLowerCase(); break;
                default: break;
            }
        }

        if (!isHttpsResponse && secure) {
            System.out.println("  [ERROR] Secure cookie sent over plain HTTP is dropped by browsers");
        }
        if (!isHttpsResponse && !secure) {
            System.out.println("  [WARN ] response over HTTP - cookie travels in clear text");
        }
        if (!httpOnly) {
            System.out.println("  [WARN ] HttpOnly missing - document.cookie can steal this value (XSS)");
        }
        if (sameSite == null) {
            System.out.println("  [WARN ] SameSite missing - modern browsers default to Lax, older ones to None");
        } else if (sameSite.equals("none") && !secure) {
            System.out.println("  [ERROR] SameSite=None requires the Secure attribute");
        }
        if (domain != null) {
            if (domain.startsWith(".")) {
                System.out.println("  [INFO ] leading dot in Domain is ignored since RFC 6265");
            }
            String registrable = registrableDomain(domain);
            if (!domain.equals(registrable)) {
                System.out.println("  [WARN ] Domain=" + domain + " widens scope to all subdomains of "
                        + registrable + " - omit Domain for host-only cookies");
            }
            if (isPublicSuffix(domain)) {
                System.out.println("  [ERROR] public suffix (" + domain + ") cannot be a cookie domain");
            }
        }
        if (name.startsWith("__Host-")) {
            boolean ok = domain == null && "/".equals(path) && secure;
            System.out.println(ok
                    ? "  [OK   ] __Host- prefix rules satisfied"
                    : "  [ERROR] __Host- requires Secure, Path=/ and NO Domain attribute");
        }
        System.out.println();
    }

    /** Very small stand-in for the Public Suffix List: last two labels. */
    private static String registrableDomain(String host) {
        String[] labels = host.replaceAll("^\\.", "").split("\\.");
        if (labels.length <= 2) {
            return host.replaceAll("^\\.", "");
        }
        return labels[labels.length - 2] + "." + labels[labels.length - 1];
    }

    private static boolean isPublicSuffix(String domain) {
        String cleaned = domain.replaceAll("^\\.", "");
        return cleaned.indexOf('.') < 0 || RESERVED_SUFFIXES.contains(cleaned);
    }

    public static void main(String[] args) {
        validate("session=abc123; Path=/; Secure; HttpOnly; SameSite=Lax", true);
        validate("prefs=dark; Path=/", true);
        validate("tracking=1; Domain=.example.com; SameSite=Lax", true);
        validate("cart=3-items; Domain=com; Path=/", true);
        validate("__Host-sid=xyz; Path=/; Secure", true);
        validate("__Host-sid=xyz; Domain=example.com; Path=/; Secure", true);
        validate("sid=abc; SameSite=None", false);
    }
}
