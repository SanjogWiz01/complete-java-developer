import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonHandlingDemo {

    public static void main(String[] args) {
        System.out.println("=== JSON Handling Demo ===\n");

        try {
            URL url = new URL("https://jsonplaceholder.typicode.com/users/1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                con.disconnect();

                String json = response.toString();
                System.out.println("Raw JSON Response:");
                System.out.println(json);
                System.out.println();

                System.out.println("Manual JSON Parsing:");
                String name = extractJsonValue(json, "name");
                String email = extractJsonValue(json, "email");
                String phone = extractJsonValue(json, "phone");

                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
                System.out.println();

                System.out.println("JSON Content Types:");
                System.out.println("text/html - HTML pages");
                System.out.println("text/plain - Plain text");
                System.out.println("application/json - JSON data");
                System.out.println("application/xml - XML data");
            }
            con.disconnect();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "not found";

        startIndex += searchKey.length();
        while (startIndex < json.length() && json.charAt(startIndex) == ' ') {
            startIndex++;
        }

        if (startIndex < json.length() && json.charAt(startIndex) == '"') {
            startIndex++;
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        }

        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = json.indexOf("}", startIndex);
        return json.substring(startIndex, endIndex).trim();
    }
}
