# GET Requests

GET is the most common HTTP method used to retrieve data from a server.

## Basic GET Request

```java
URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("GET");

BufferedReader br = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String response = br.readLine();
System.out.println(response);
```

## GET with Query Parameters

```java
String baseUrl = "https://api.example.com/search";
String query = "java";
String params = "?q=" + query + "&page=1";
URL url = new URL(baseUrl + params);
```

## Reading Response Code

```java
int status = con.getResponseCode();
if (status == 200) {
    // Read response
} else {
    System.out.println("Error: " + status);
}
```

## Best Practices

- Always check the response status code.
- Close connections using `disconnect()` or try-with-resources.
- Set timeouts to avoid hanging indefinitely.
- Handle exceptions for network errors.
