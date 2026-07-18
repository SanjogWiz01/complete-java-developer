import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ApiAsyncDemo {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        System.out.println("=== API Async Demo ===\n");

        System.out.println("1. Sequential API Calls:");
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 3; i++) {
            String result = fetchSync("https://jsonplaceholder.typicode.com/posts/" + i);
            System.out.println("  Post " + i + ": " + result.length() + " chars");
        }
        long sequentialTime = System.currentTimeMillis() - start;
        System.out.println("  Sequential time: " + sequentialTime + "ms\n");

        System.out.println("2. Parallel API Calls with CompletableFuture:");
        start = System.currentTimeMillis();
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            final int postId = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return fetchSync("https://jsonplaceholder.typicode.com/posts/" + postId);
            }, executor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (int i = 0; i < futures.size(); i++) {
            try {
                String result = futures.get(i).get();
                System.out.println("  Post " + (i + 1) + ": " + result.length() + " chars");
            } catch (Exception e) {
                System.out.println("  Error: " + e.getMessage());
            }
        }
        long parallelTime = System.currentTimeMillis() - start;
        System.out.println("  Parallel time: " + parallelTime + "ms\n");

        System.out.println("3. Async with Callback:");
        CompletableFuture.supplyAsync(() -> {
            return fetchSync("https://jsonplaceholder.typicode.com/posts/1");
        }, executor).thenAccept(result -> {
            System.out.println("  Callback received: " + result.length() + " chars");
        }).exceptionally(ex -> {
            System.out.println("  Error in callback: " + ex.getMessage());
            return null;
        });

        System.out.println("4. Async with Timeout:");
        try {
            String result = CompletableFuture.supplyAsync(() -> {
                return fetchSync("https://jsonplaceholder.typicode.com/posts/2");
            }, executor).orTimeout(5, TimeUnit.SECONDS).get();
            System.out.println("  Got result: " + result.length() + " chars");
        } catch (Exception e) {
            System.out.println("  Timeout or error: " + e.getMessage());
        }

        System.out.println("\n5. Chained Async Calls:");
        CompletableFuture.supplyAsync(() -> {
            return fetchSync("https://jsonplaceholder.typicode.com/posts/1");
        }, executor).thenApply(result -> {
            System.out.println("  Step 1 - Fetched post: " + result.length() + " chars");
            return result;
        }).thenApply(result -> {
            System.out.println("  Step 2 - Processing: " + result.substring(0, Math.min(50, result.length())) + "...");
            return result.toUpperCase().length();
        }).thenAccept(length -> {
            System.out.println("  Step 3 - Final result length: " + length);
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
    }

    private static String fetchSync(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

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
                return response.toString();
            }
            con.disconnect();
            return "Error: status " + status;

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
