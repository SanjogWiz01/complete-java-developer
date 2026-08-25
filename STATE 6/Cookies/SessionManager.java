import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session Manager (Java 8)
 * ------------------------
 * Server-side sessions are the stateful counterpart to cookies:
 *
 *   - The server creates a Session object and stores it in a map.
 *   - Only the random session ID travels to the browser - inside a
 *     single cookie named JSESSIONID (the same name Tomcat uses).
 *   - Every later request presents the ID, the server looks the session up.
 *
 * This class is thread-safe (ConcurrentHashMap) and expires sessions
 * after 30 minutes of inactivity, exactly like a servlet container.
 */
public class SessionManager {

    /** Everything the server remembers about one browser. */
    public static final class Session {
        private final String id;
        private final Instant createdAt;
        private volatile Instant lastAccessed;
        private final Map<String, Object> attributes = new ConcurrentHashMap<>();

        Session(String id) {
            this.id = id;
            this.createdAt = Instant.now();
            this.lastAccessed = createdAt;
        }

        public String getId()                    { return id; }
        public Instant getCreatedAt()            { return createdAt; }
        public Instant getLastAccessed()         { return lastAccessed; }
        public Map<String, Object> attributes()  { return attributes; }

        void touch()                             { lastAccessed = Instant.now(); }

        @Override
        public String toString() {
            return "Session{id=" + id.substring(0, 8) + "..., attrs=" + attributes + "}";
        }
    }

    private static final long DEFAULT_TIMEOUT_SECONDS = 30 * 60;   // like Tomcat

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final long timeoutSeconds;

    public SessionManager() {
        this(DEFAULT_TIMEOUT_SECONDS);
    }

    public SessionManager(long timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    /* ------------------------- lifecycle ------------------------- */

    /** Creates a fresh session with an unpredictable ID. */
    public Session create() {
        String id = UUID.randomUUID().toString().replace("-", "");
        Session session = new Session(id);
        sessions.put(id, session);
        return session;
    }

    /**
     * Finds a live session by its ID.
     * Expired sessions are removed lazily on access.
     */
    public Optional<Session> find(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return Optional.empty();
        }
        Session session = sessions.get(sessionId);
        if (session == null) {
            return Optional.empty();
        }
        if (isExpired(session)) {
            sessions.remove(sessionId);
            return Optional.empty();
        }
        session.touch();
        return Optional.of(session);
    }

    /** find() + auto-create in one call - mirrors HttpServletRequest.getSession(true). */
    public Session getOrCreate(String sessionId) {
        return find(sessionId).orElseGet(this::create);
    }

    /** Explicit logout / invalidation. */
    public boolean invalidate(String sessionId) {
        return sessions.remove(sessionId) != null;
    }

    /** Removes every session idle longer than the timeout (call from a timer). */
    public int purgeExpired() {
        int before = sessions.size();
        sessions.values().removeIf(this::isExpired);
        return before - sessions.size();
    }

    public int activeCount() {
        return sessions.size();
    }

    /* ------------------------- internals ------------------------- */

    private boolean isExpired(Session session) {
        return session.getLastAccessed().plusSeconds(timeoutSeconds).isBefore(Instant.now());
    }

    /* --------------------------- demo ---------------------------- */

    public static void main(String[] args) throws InterruptedException {
        SessionManager manager = new SessionManager(2);          // 2s timeout for the demo

        // Request 1: unknown browser -> new session is born.
        Session first = manager.getOrCreate(null);
        first.attributes().put("user", "sanjog");
        first.attributes().put("cartItems", 3);
        System.out.println("created : " + first);

        // Request 2: same ID returns the SAME session object with data intact.
        Optional<Session> again = manager.find(first.getId());
        System.out.println("found   : " + again.map(Object::toString).orElse("<expired>"));
        System.out.println("same?   : " + (again.isPresent() && again.get() == first));

        // Unknown / tampered IDs yield nothing.
        System.out.println("bogus   : " + manager.find("deadbeef"));

        // Wait out the timeout -> session silently dies.
        Thread.sleep(2500);
        System.out.println("after timeout: " + manager.find(first.getId()));
        System.out.println("active sessions now: " + manager.activeCount());
    }
}
