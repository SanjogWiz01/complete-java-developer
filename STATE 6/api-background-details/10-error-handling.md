# Error Handling in API Calls

Proper error handling makes API integrations reliable and debuggable.

## Common Errors

| Error | Cause |
|-------|-------|
| `IOException` | Network connectivity issues |
| `SocketTimeoutException` | Request took too long |
| `HttpURLConnection` error codes | Server returned an error status |

## Basic Error Handling

```java
try {
    URL url = new URL("https://api.example.com/data");
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");

    int status = con.getResponseCode();
    if (status == 200) {
        // Read success response
    } else {
        // Read error stream
        BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getErrorStream()));
        String error = br.readLine();
        System.out.println("Error " + status + ": " + error);
    }
} catch (MalformedURLException e) {
    System.out.println("Invalid URL");
} catch (SocketTimeoutException e) {
    System.out.println("Request timed out");
} catch (IOException e) {
    System.out.println("Network error: " + e.getMessage());
} finally {
    con.disconnect();
}
```

## Reading Error Stream

```java
if (con.getResponseCode() >= 400) {
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getErrorStream()))) {
        StringBuilder error = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            error.append(line);
        }
        System.out.println("Error response: " + error.toString());
    }
}
```

## Retry Strategy

- Implement exponential backoff for transient errors.
- Limit the number of retry attempts.
- Do not retry on 4xx errors (client errors).
- Retry on 5xx errors (server errors) and network timeouts.
