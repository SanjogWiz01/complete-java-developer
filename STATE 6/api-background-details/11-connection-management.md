# Connection Management

Efficient connection management improves performance and prevents resource leaks.

## Setting Timeouts

```java
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setConnectTimeout(5000);  // 5 seconds to connect
con.setReadTimeout(5000);     // 5 seconds to read response
```

## Try-With-Resources

```java
try (BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream()))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}  // Auto-closed
```

## Disconnecting

```java
try {
    // Use the connection
} finally {
    con.disconnect();
}
```

## Connection Pooling with OkHttp

```java
OkHttpClient client = new OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
    .build();
```

## Best Practices

- Always set connect and read timeouts.
- Close streams and connections when done.
- Reuse HTTP clients when making multiple requests.
- Avoid creating a new `HttpURLConnection` for each request in loops.
- Use connection pooling for production applications.

## Memory Leaks to Avoid

- Not closing input/output streams.
- Not disconnecting `HttpURLConnection`.
- Keeping references to old connections.
- Not handling error streams properly.
