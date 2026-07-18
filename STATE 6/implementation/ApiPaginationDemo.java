import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiPaginationDemo {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final int PAGE_SIZE = 5;

    public static void main(String[] args) {
        System.out.println("=== API Pagination Demo ===\n");
        System.out.println("Fetching posts with pagination (page size: " + PAGE_SIZE + ")\n");

        int totalPages = (int) Math.ceil(100.0 / PAGE_SIZE);

        for (int page = 1; page <= 3; page++) {
            System.out.println("--- Page " + page + " ---");
            fetchPage(page, PAGE_SIZE);
            System.out.println();
        }

        System.out.println("=== Offset-based Pagination ===\n");
        for (int offset = 0; offset < 15; offset += PAGE_SIZE) {
            System.out.println("Offset: " + offset + ", Limit: " + PAGE_SIZE);
            fetchWithOffset(offset, PAGE_SIZE);
            System.out.println();
        }
    }

    private static void fetchPage(int page, int limit) {
        try {
            int start = (page - 1) * limit;
            String urlString = BASE_URL + "?_start=" + start + "&_limit=" + limit;
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            System.out.println("URL: " + urlString);
            System.out.println("Status: " + status);

            String totalCount = con.getHeaderField("X-Total-Count");
            if (totalCount != null) {
                System.out.println("Total Items: " + totalCount);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Items received: " + response.toString().length() + " chars");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void fetchWithOffset(int offset, int limit) {
        try {
            String urlString = BASE_URL + "?_start=" + offset + "&_limit=" + limit;
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            System.out.println("URL: " + urlString);
            System.out.println("Status: " + status);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Response size: " + response.length() + " chars");

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
