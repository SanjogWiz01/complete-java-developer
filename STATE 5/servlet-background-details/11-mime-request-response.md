# MIME Request and Response Handling

Servlet containers help decode incoming request data and encode outgoing response data.

MIME types tell the browser what kind of content is being returned.

## Common Content Types

- `text/html` for HTML pages.
- `text/plain` for plain text.
- `application/json` for JSON APIs.
- `application/pdf` for PDF files.

## Response Example

```java
response.setContentType("application/json");
response.getWriter().println("{\"status\":\"ok\"}");
```

## Why It Matters

If the wrong content type is used, the browser or client application may display or parse the response incorrectly.
