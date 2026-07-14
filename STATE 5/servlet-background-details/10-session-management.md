# Session Management

HTTP is stateless, so every request is independent by default.

Session management lets the server remember user data across multiple requests.

## Common Techniques

- Cookies.
- URL rewriting.
- Hidden form fields.
- `HttpSession`.

## HttpSession Example

```java
HttpSession session = request.getSession();
session.setAttribute("username", "sanjog");
```

Later requests can read the same attribute:

```java
String username = (String) session.getAttribute("username");
```

## Common Uses

- Login state.
- Shopping cart data.
- User preferences.
- Multi-step form progress.
