# Basic Servlet Program

A basic servlet extends `HttpServlet` and overrides request handling methods such as `doGet` or `doPost`.

## Example

```java
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>Hello from Servlet</h1>");
    }
}
```

## Required Setup

- JDK installed.
- Apache Tomcat installed.
- Servlet API available through Tomcat.
- Project packaged as a web application.
