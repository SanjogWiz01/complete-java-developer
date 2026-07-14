# DELETE Requests

DELETE is used to remove a resource from the server.

## Basic DELETE Request

```java
URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
HttpURLConnection con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("DELETE");

int status = con.getResponseCode();
System.out.println("Status: " + status);
```

## DELETE with Response Body

```java
int status = con.getResponseCode();
if (status == 200) {
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line.trim());
        }
        System.out.println("Deleted: " + response.toString());
    }
} else if (status == 404) {
    System.out.println("Resource not found");
}
```

## Common DELETE Response Codes

- **200 OK** - Deleted successfully, response body included
- **204 No Content** - Deleted successfully, no response body
- **404 Not Found** - Resource does not exist
- **403 Forbidden** - Not allowed to delete

## Key Points

- DELETE is idempotent - deleting the same resource multiple times has the same effect.
- Some APIs return the deleted resource in the response body.
- Some APIs return 204 No Content on successful deletion.
- Always handle error responses for failed deletions.
