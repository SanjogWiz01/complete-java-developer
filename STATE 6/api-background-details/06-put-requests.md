# PUT Requests

PUT is used to update an existing resource on the server.

## Basic PUT Request

```java
URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("PUT");
con.setRequestProperty("Content-Type", "application/json; utf-8");
con.setDoOutput(true);

String jsonInput = "{\"id\": 1, \"title\": \"Updated Title\", \"body\": \"Updated content\", \"userId\": 1}";

try (OutputStream os = con.getOutputStream()) {
    byte[] input = jsonInput.getBytes("utf-8");
    os.write(input, 0, input.length);
}

int status = con.getResponseCode();
System.out.println("Status: " + status);
```

## PUT vs PATCH

| Feature | PUT | PATCH |
|---------|-----|-------|
| Purpose | Replace entire resource | Partial update |
| Body | Complete resource representation | Only changed fields |
| Idempotent | Yes | Not necessarily |

## Reading PUT Response

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

- PUT replaces the entire resource - send all fields.
- PUT is idempotent - sending the same request multiple times produces the same result.
- The URL specifies which resource to update.
