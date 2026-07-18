import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiRateLimitingDemo {

    private static final Semaphore rateLimiter = new Semaphore(3);
    private static final AtomicInteger requestCount = new AtomicInteger(0);
    private static final long WINDOW_SIZE = 1000;
    private static final int MAX_REQUESTS_PER_WINDOW = 5;

    private static long windowStart = System.currentTimeMillis();
    private static int requestsInWindow = 0;

    public static void main(String[] args) {
        System.out.println("=== API Rate Limiting Demo ===\n");

        System.out.println("1. Semaphore-based Rate Limiting (max 3 concurrent):");
        for (int i = 0; i < 6; i++) {
            final int id = i + 1;
            new Thread(() -> {
                semaphoreRateLimit("https://jsonplaceholder.typicode.com/posts/" + id, id);
            }).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n2. Fixed Window Rate Limiting (max 5 per second):");
        for (int i = 1; i <= 8; i++) {
            fixedWindowRateLimit("https://jsonplaceholder.typicode.com/posts/" + i, i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n3. Token Bucket Rate Limiting:");
        TokenBucket bucket = new TokenBucket(5, 1);
        for (int i = 1; i <= 7; i++) {
            final int id = i;
            if (bucket.tryAcquire()) {
                System.out.println("  Request " + id + ": Allowed");
                fetchAsync("https://jsonplaceholder.typicode.com/posts/" + id);
            } else {
                System.out.println("  Request " + id + ": Denied (rate limited)");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void semaphoreRateLimit(String urlString, int id) {
        try {
            rateLimiter.acquire();
            System.out.println("  Request " + id + ": Acquired permit");
            long start = System.currentTimeMillis();

            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("  Request " + id + ": Status " + status + " (" + elapsed + "ms)");

            con.disconnect();
            rateLimiter.release();

        } catch (IOException e) {
            System.out.println("  Request " + id + ": Error - " + e.getMessage());
            rateLimiter.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            rateLimiter.release();
        }
    }

    private static void fixedWindowRateLimit(String urlString, int id) {
        long now = System.currentTimeMillis();

        synchronized (ApiRateLimitingDemo.class) {
            if (now - windowStart > WINDOW_SIZE) {
                windowStart = now;
                requestsInWindow = 0;
            }
        }

        if (requestsInWindow >= MAX_REQUESTS_PER_WINDOW) {
            System.out.println("  Request " + id + ": Rate limited (window full)");
            return;
        }

        requestsInWindow++;
        System.out.println("  Request " + id + ": Allowed (" + requestsInWindow + "/" + MAX_REQUESTS_PER_WINDOW + ")");
        fetchAsync(urlString);
    }

    private static void fetchAsync(String urlString) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int status = con.getResponseCode();
                con.disconnect();
            } catch (IOException e) {
                // silent
            }
        }).start();
    }

    static class TokenBucket {
        private int tokens;
        private final int maxTokens;
        private final long refillRate;
        private long lastRefillTime;

        TokenBucket(int maxTokens, long refillRatePerSecond) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRatePerSecond;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean tryAcquire() {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            int tokensToAdd = (int) (elapsed * refillRate / 1000);
            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }
    }
}
