# 03 - HttpClient Forge: Java HTTP Clients

## Game Name: HttpClient Forge

## What You Learn
- HttpURLConnection (legacy approach)
- HttpClient (Java 11+, recommended)
- OkHttp library
- Apache HttpClient library
- When to use each client

## Key Concepts

### Comparison Table

| Client | Java Version | Async Support | External Dep | Recommended |
|--------|-------------|---------------|--------------|-------------|
| HttpURLConnection | Any | No | No | No (outdated) |
| HttpClient | 11+ | Yes | No | Yes |
| OkHttp | Any | Yes | Yes | Yes |
| Apache HttpClient | Any | Yes | Yes | Yes |

### HttpClient (Java 11+) - Recommended
```java
HttpClient client = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .build();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Accept", "application/json")
    .GET()
    .build();

HttpResponse<String> response = client.send(
    request, HttpResponse.BodyHandlers.ofString());
```

### OkHttp
- Lightweight and fast
- Supports HTTP/2 and WebSockets
- Connection pooling built-in

### Apache HttpClient
- Most feature-rich
- Connection pooling and proxy support
- Heavier dependency

## Run This File
```bash
javac 03_HttpClientForge.java
java HttpClientForge
```

## Next Topic
[04 - REST Principles Game](04_RestPrinciplesGame.md) - RESTful API Design
