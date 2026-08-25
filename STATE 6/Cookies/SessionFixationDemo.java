import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Session Fixation Attack & Defense (Java 8)
 * ------------------------------------------
 * Session fixation is the attack where an attacker plants a KNOWN session
 * id in a victim's browser (via link, subdomain cookie, ...) and waits for
 * the victim to log in. If the server keeps the same id after login, the
 * attacker now owns an authenticated session.
 *
 * Defense: ALWAYS regenerate the session id at privilege change
 * (login, password change, permission escalation) and delete the old one.
 *
 * This demo shows a vulnerable flow vs a fixed flow side by side.
 *
 * Run:  java SessionFixationDemo
 */
public class SessionFixationDemo {

    static final class LoginService {
        private final Map<String, String> sessions = new LinkedHashMap<>();
        private final boolean regeneratesId;
        private final Map<String, String> users = new LinkedHashMap<>();

        LoginService(boolean regeneratesId) {
            this.regeneratesId = regeneratesId;
            users.put("sanjog", "password123");
        }

        /** Step 1: anonymous visit - server hands out a session id. */
        String createAnonymousSession() {
            String id = UUID.randomUUID().toString();
            sessions.put(id, "role=guest");
            return id;
        }

        /**
         * Step 2: login.
         * vulnerable=true  -> keeps the attacker-chosen id  (BUG)
         * vulnerable=false -> issues a brand new id         (FIX)
         */
        Optional<String> login(String sessionId, String user, String password) {
            if (!"password123".equals(password) || !users.containsKey(user)) {
                return Optional.empty();
            }
            String effective = sessionId != null && sessions.containsKey(sessionId) ? sessionId : null;
            if (!regeneratesId && effective == null) {
                return Optional.empty();
            }
            if (regeneratesId) {
                if (effective != null) {
                    sessions.remove(effective);
                }
                String fresh = UUID.randomUUID().toString();
                sessions.put(fresh, "role=user;user=" + user);
                return Optional.of(fresh);
            }
            sessions.put(effective, "role=user;user=" + user);
            return Optional.of(effective);
        }
    }

    private static void runScenario(String title, boolean regeneratesId) {
        System.out.println("--- " + title + " ---");
        LoginService app = new LoginService(regeneratesId);

        String attackerKnownId = app.createAnonymousSession();
        System.out.println("Attacker plants session id : " + abbrev(attackerKnownId));

        String victimSession = app.login(attackerKnownId, "sanjog", "password123").orElse("?");
        System.out.println("Victim logs in, browser uses: " + abbrev(victimSession));

        if (victimSession.equals(attackerKnownId)) {
            System.out.println("Result: SAME id -> attacker can reuse it and hijack the account!");
        } else {
            System.out.println("Result: NEW id   -> attacker's copy points to a deleted guest session.");
        }
        System.out.println();
    }

    private static String abbrev(String id) {
        return id.substring(0, 8) + "...";
    }

    public static void main(String[] args) {
        runScenario("Vulnerable server (id reused after login)", false);
        runScenario("Hardened server (id regenerated on login)", true);

        java.util.function.Supplier<String> rotation = () -> UUID.randomUUID().toString();
        System.out.println("Rotate ids on every privilege change: login=" + abbrev(rotation.get())
                + ", password-change=" + abbrev(rotation.get()));
    }
}
