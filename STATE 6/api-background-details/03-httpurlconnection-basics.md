# HttpURLConnection Basics

`java.net.HttpURLConnection` is the built-in Java class for making HTTP requests without external libraries.

## Creating a Connection

```java
URL url = new URL("https://api.example.com/data");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
```

## Setting Request Properties

```java
con.setRequestMethod("GET");
con.setRequestProperty("Content-Type", "application/json");
con.setConnectTimeout(5000);
con.setReadTimeout(5000);
```

## Reading the Response

```java
int status = con.getResponseCode();
BufferedReader br = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String line;
while ((line = br.readLine()) != null) {
    System.out.println(line);
}
```

## Important Methods

- `getResponseCode()` - Returns HTTP status code
- `getRequestMethod()` - Returns the request method
- `getInputStream()` - Returns input stream for reading response
- `getOutputStream()` - Returns output stream for sending data
- `disconnect()` - Closes the connection

## When to Use

- Simple API calls without complex requirements
- Learning and prototyping
- When external libraries are not available

## Limitations

- No built-in connection pooling
- Manual error handling required
- Limited support for modern HTTP features
- verbose boilerplate code
