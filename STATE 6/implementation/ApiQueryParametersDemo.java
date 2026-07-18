import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiQueryParametersDemo {

    public static void main(String[] args) {
        try {
            String baseUrl = "https://jsonplaceholder.typicode.com/posts";
            String userId = "1";
            String sortParam = "id";
            String orderParam = "desc";
            String searchQuery = URLEncoder.encode("qui est", "UTF-8");

            String queryString = String.format(
                "?userId=%s&_sort=%s&_order=%s&q=%s",
                userId, sortParam, orderParam, searchQuery
            );

            String fullUrl = baseUrl + queryString;
            System.out.println("=== API Query Parameters Demo ===");
            System.out.println("Base URL: " + baseUrl);
            System.out.println("Parameters:");
            System.out.println("  userId = " + userId);
            System.out.println("  _sort = " + sortParam);
            System.out.println("  _order = " + orderParam);
            System.out.println("  q = " + searchQuery);
            System.out.println("Full URL: " + fullUrl);
            System.out.println();

            URL url = new URL(fullUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            System.out.println("Response Status: " + status);

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            con.disconnect();

            System.out.println("Response Body: " + response.toString());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
