import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Session Persistence to Disk (Java 8)
 * ------------------------------------
 * In-memory sessions (see SessionManager) vanish on restart or deploy.
 * Real containers offer persistence; this demo builds a minimal version:
 *
 *   - serialize sessions to a simple "key=value lines" file
 *   - reload them at startup, keeping idle-timeout semantics
 *   - show why session data must be serializable & small
 *
 * Format per session:
 *   id|user|createdAtEpochMillis|lastSeenEpochMillis
 *
 * Run:  java SessionPersistence   (writes session-store.txt next to the class)
 */
public class SessionPersistence {

    static final class StoredSession {
        final String id;
        String user;
        final Instant createdAt;
        Instant lastSeen;

        StoredSession(String id, String user) {
            this.id = id;
            this.user = user;
            this.createdAt = Instant.now();
            this.lastSeen = createdAt;
        }

        StoredSession(String id, String user, Instant createdAt, Instant lastSeen) {
            this.id = id;
            this.user = user;
            this.createdAt = createdAt;
            this.lastSeen = lastSeen;
        }

        boolean isExpired(long timeoutSeconds) {
            return lastSeen.plusSeconds(timeoutSeconds).isBefore(Instant.now());
        }

        String serialize() {
            return id + "|" + user + "|" + createdAt.toEpochMilli() + "|" + lastSeen.toEpochMilli();
        }
    }

    private final Map<String, StoredSession> sessions = new LinkedHashMap<>();
    private final Path file;

    public SessionPersistence(Path file) {
        this.file = file;
    }

    public String create(String user) {
        StoredSession s = new StoredSession(java.util.UUID.randomUUID().toString(), user);
        sessions.put(s.id, s);
        return s.id;
    }

    /** Simulates activity: touching the session extends its life. */
    public Optional<StoredSession> touch(String id) {
        StoredSession s = sessions.get(id);
        if (s != null) {
            s.lastSeen = Instant.now();
        }
        return Optional.ofNullable(s);
    }

    public int save() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (StoredSession s : sessions.values()) {
            sb.append(s.serialize()).append(System.lineSeparator());
        }
        Files.write(file, sb.toString().getBytes(StandardCharsets.UTF_8));
        return sessions.size();
    }

    public int load(long timeoutSeconds) throws IOException {
        if (!Files.exists(file)) {
            return 0;
        }
        int restored = 0;
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] p = line.split("\\|", -1);
            if (p.length != 4) {
                System.out.println("Skipping corrupt line: " + line);
                continue;
            }
            StoredSession s = new StoredSession(p[0], p[1],
                    Instant.ofEpochMilli(Long.parseLong(p[2])),
                    Instant.ofEpochMilli(Long.parseLong(p[3])));
            if (!s.isExpired(timeoutSeconds)) {
                sessions.put(s.id, s);
                restored++;
            } else {
                System.out.println("Discarding expired session of '" + s.user + "'");
            }
        }
        return restored;
    }

    public void printAll() {
        sessions.values().forEach(s ->
                System.out.println("  " + s.id.substring(0, 8) + "... user=" + s.user
                        + " lastSeen=" + s.lastSeen));
    }

    public static void main(String[] args) throws Exception {
        Path storeFile = Paths.get("session-store.txt");

        SessionPersistence server1 = new SessionPersistence(storeFile);
        server1.create("sanjog");
        Thread.sleep(5);
        server1.create("priya");
        System.out.println("Server running with sessions:");
        server1.printAll();
        System.out.println("Saved " + server1.save() + " session(s) to " + storeFile.getFileName());

        System.out.println();
        System.out.println("--- restart ---");
        SessionPersistence server2 = new SessionPersistence(storeFile);
        long oneHour = 3600;
        System.out.println("Restored " + server2.load(oneHour) + " active session(s):");
        server2.printAll();

        System.out.println();
        Path tinyTimeoutStore = Paths.get("session-store.txt");
        SessionPersistence strictServer = new SessionPersistence(tinyTimeoutStore);
        System.out.println("Restoring with 0s timeout (all idle too long): "
                + strictServer.load(0) + " session(s)");

        Files.deleteIfExists(storeFile);
    }
}
