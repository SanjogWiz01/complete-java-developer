# POST Requests

POST is used to send data to the server to create a new resource.

## Basic POST Request

```java
URL url = new URL("https://jsonplaceholder.typicode.com/posts");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("POST");
con.setRequestProperty("Content-Type", "application/json; utf-8");
con.setDoOutput(true);

String jsonInput = "{\"title\": \"Hello\", \"body\": \"Content\", \"userId\": 1}";

try (OutputStream os = con.getOutputStream()) {
    byte[] input = jsonInput.getBytes("utf-8");
    os.write(input, 0, input.length);
}

int status = con.getResponseCode();
System.out.println("Status: " + status);
```

## Sending Form Data

```java
con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
String formData = "username=sanjog&password=secret";
try (OutputStream os = con.getOutputStream()) {
    os.write(formData.getBytes("utf-8"));
}
```

## Reading POST Response

```java
try (BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"))) {
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = br.readLine()) != null) {
        response.append(line.trim());
    }
    System.out.println(response.toString());
}
```

## Key Points

- `setDoOutput(true)` is required for POST requests.
- Set `Content-Type` header to tell the server what data format you are sending.
- POST is not idempotent - sending the same request multiple times may create multiple resources.
