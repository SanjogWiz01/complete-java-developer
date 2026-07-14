# Request Response Lifecycle

The servlet request-response lifecycle explains how a browser request becomes a server response.

## Workflow

1. The client sends an HTTP request.
2. The web server receives the request.
3. The server forwards the request to the servlet container.
4. The container selects the matching servlet.
5. The servlet processes the request.
6. The servlet writes output to the response.
7. The container sends the response back to the web server.
8. The browser displays the result.

## Main Objects

- `HttpServletRequest` stores incoming request data.
- `HttpServletResponse` builds the response sent to the client.

The request object is for reading. The response object is for writing.
