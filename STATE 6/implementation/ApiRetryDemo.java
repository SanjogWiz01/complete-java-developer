import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRetryDemo {

    private static final int MAX_RETRIES = 3;
    private static final long BASE_DELAY = 1000;

    public static void main(String[] args) {
        System.out.println("=== API Retry Demo ===\n");

        System.out.println("1. Simple Retry:");
        String result1 = fetchWithRetry("https://jsonplaceholder.typicode.com/posts/1");
        System.out.println("Result length: " + (result1 != null ? result1.length() : 0) + " chars\n");

        System.out.println("2. Retry with Exponential Backoff:");
        String result2 = fetchWithExponentialBackoff("https://jsonplaceholder.typicode.com/posts/1");
        System.out.println("Result length: " + (result2 != null ? result2.length() : 0) + " chars\n");

        System.out.println("3. Retry with Status Code Check:");
        String result3 = fetchWithStatusRetry("https://jsonplaceholder.typicode.com/posts/99999");
        System.out.println("Result: " + result3 + "\n");
    }

    private static String fetchWithRetry(String urlString) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            attempts++;
            try {
                System.out.println("  Attempt " + attempts + "/" + MAX_RETRIES);
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
                    System.out.println("  Success on attempt " + attempts);
                    return response.toString();
                }

                con.disconnect();
                System.out.println("  Got status " + status + ", retrying...");

            } catch (IOException e) {
                System.out.println("  Error on attempt " + attempts + ": " + e.getMessage());
                try {
                    Thread.sleep(BASE_DELAY);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        System.out.println("  Failed after " + MAX_RETRIES + " attempts");
        return null;
    }

    private static String fetchWithExponentialBackoff(String urlString) {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            attempts++;
            try {
                long delay = BASE_DELAY * (long) Math.pow(2, attempts - 1);
                System.out.println("  Attempt " + attempts + " (delay: " + delay + "ms)");

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
                    System.out.println("  Success on attempt " + attempts);
                    return response.toString();
                }

                con.disconnect();
                System.out.println("  Got status " + status + ", waiting " + delay + "ms before retry...");
                Thread.sleep(delay);

            } catch (IOException e) {
                System.out.println("  Error on attempt " + attempts + ": " + e.getMessage());
                try {
                    long delay = BASE_DELAY * (long) Math.pow(2, attempts - 1);
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return null;
    }

    private static String fetchWithStatusRetry(String urlString) {
        int attempts = 0;
        int[] retryableStatusCodes = {429, 500, 502, 503, 504};

        while (attempts < MAX_RETRIES) {
            attempts++;
            try {
                System.out.println("  Attempt " + attempts);
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);

                int status = con.getResponseCode();
                System.out.println("  Status: " + status);

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

                boolean shouldRetry = false;
                for (int retryable : retryableStatusCodes) {
                    if (status == retryable) {
                        shouldRetry = true;
                        break;
                    }
                }

                con.disconnect();
                if (!shouldRetry) {
                    System.out.println("  Non-retryable status code: " + status);
                    return "Non-retryable status: " + status;
                }

                Thread.sleep(BASE_DELAY * attempts);

            } catch (IOException e) {
                System.out.println("  Error: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return "All retries exhausted";
    }
}
