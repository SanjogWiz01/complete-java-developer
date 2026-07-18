import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApiCachingDemo {

    private static final Map<String, CacheEntry> cache = new HashMap<>();
    private static final long CACHE_TTL = 30000;

    static class CacheEntry {
        String data;
        long timestamp;

        CacheEntry(String data) {
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== API Caching Demo ===\n");

        String url = "https://jsonplaceholder.typicode.com/posts/1";

        System.out.println("1. First request (cache miss):");
        String result1 = fetchWithCache(url);
        System.out.println("Result length: " + result1.length() + " chars\n");

        System.out.println("2. Second request (cache hit):");
        String result2 = fetchWithCache(url);
        System.out.println("Result length: " + result2.length() + " chars\n");

        System.out.println("3. Cache status:");
        System.out.println("Cache size: " + cache.size());
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            System.out.println("  URL: " + entry.getKey());
            System.out.println("  Expired: " + entry.getValue().isExpired());
            System.out.println("  Age: " + (System.currentTimeMillis() - entry.getValue().timestamp) + "ms");
        }
        System.out.println();

        System.out.println("4. Cache validation with ETag:");
        fetchWithETag("https://jsonplaceholder.typicode.com/posts/1");

        System.out.println("\n5. Cache-Control headers:");
        demonstrateCacheHeaders("https://jsonplaceholder.typicode.com/posts/1");
    }

    private static String fetchWithCache(String urlString) {
        CacheEntry cached = cache.get(urlString);
        if (cached != null && !cached.isExpired()) {
            System.out.println("  Cache HIT - returning cached data");
            return cached.data;
        }

        System.out.println("  Cache MISS - fetching from API");
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                con.disconnect();

                String data = response.toString();
                cache.put(urlString, new CacheEntry(data));
                System.out.println("  Cached response (" + data.length() + " chars)");
                return data;
            }
            con.disconnect();

        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
        return "";
    }

    private static void fetchWithETag(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            String etag = con.getHeaderField("ETag");
            System.out.println("  ETag: " + etag);
            System.out.println("  Status: " + status);

            con.disconnect();

            if (etag != null) {
                HttpURLConnection con2 = (HttpURLConnection) url.openConnection();
                con2.setRequestMethod("GET");
                con2.setRequestProperty("If-None-Match", etag);

                int status2 = con2.getResponseCode();
                System.out.println("  Conditional request status: " + status2);

                if (status2 == 304) {
                    System.out.println("  Resource not modified - using cached version");
                } else {
                    System.out.println("  Resource modified - fetching new version");
                }
                con2.disconnect();
            }

        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }

    private static void demonstrateCacheHeaders(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            System.out.println("  Cache-Control: " + con.getHeaderField("Cache-Control"));
            System.out.println("  Expires: " + con.getHeaderField("Expires"));
            System.out.println("  Last-Modified: " + con.getHeaderField("Last-Modified"));
            System.out.println("  ETag: " + con.getHeaderField("ETag"));

            con.disconnect();

        } catch (IOException e) {
            System.out.println("  Error: " + e.getMessage());
        }
    }
}
