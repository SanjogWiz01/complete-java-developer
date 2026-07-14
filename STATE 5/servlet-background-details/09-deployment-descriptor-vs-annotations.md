# Deployment Descriptor vs Annotations

Servlet mappings can be configured using `web.xml` or annotations.

## Annotation Mapping

```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
}
```

This is short and useful for small applications.

## Deployment Descriptor Mapping

```xml
<servlet>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>HelloServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>
```

`web.xml` is useful when configuration should be centralized.
