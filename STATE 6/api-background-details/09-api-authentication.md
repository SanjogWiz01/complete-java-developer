# API Authentication

Most APIs require authentication to verify the identity of the client.

## Types of Authentication

### API Key

```java
con.setRequestProperty("X-API-Key", "your-api-key-here");
```

### Basic Authentication

```java
String credentials = "username:password";
String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
con.setRequestProperty("Authorization", "Basic " + encoded);
```

### Bearer Token (JWT)

```java
con.setRequestProperty("Authorization", "Bearer " + token);
```

### OAuth 2.0

```java
// Step 1: Get access token from auth server
// Step 2: Use token in requests
con.setRequestProperty("Authorization", "Bearer " + accessToken);
```

## Token Lifecycle

1. Client sends credentials to auth server.
2. Auth server validates and returns a token.
3. Client includes token in subsequent requests.
4. Server validates token and processes request.
5. Token expires after a set time.

## Security Best Practices

- Never hardcode API keys in source code.
- Use environment variables or configuration files.
- Always use HTTPS for API communication.
- Rotate tokens and keys regularly.
- Store sensitive credentials securely.
