# Client Session and Cookie Management

HTTP is stateless. `ClientSessionManager` and `ClientCookieManager` give the
client layer a stateful experience on top of the servlet container's
`HttpSession` and `Cookie` APIs.

## Session (Server Side)

Stored on the server, referenced by a `JSESSIONID` cookie sent with each request.

```java
ClientSessionManager sessionManager = new ClientSessionManager(request);

sessionManager.login(user);            // store ClientUser, 30 minute timeout
sessionManager.logout();               // invalidate the whole session
boolean loggedIn = sessionManager.isLoggedIn();
ClientUser user = sessionManager.getLoggedInUser();
```

### What Lives in the Session

| Data | Key handled by |
| --- | --- |
| Logged-in `ClientUser` | `ClientSessionManager` |
| `ClientShoppingCart` | `ClientSessionManager.getCart()` (lazy creation) |
| CSRF token | `ClientSessionManager` |
| One-shot flash messages | `flashMessage()` / `consumeFlashMessage()` |

Flash messages follow the pattern: set after a redirect, read once on the next
page, removed immediately.

## Cookies (Browser Side)

```java
ClientCookieManager cookieManager = new ClientCookieManager(request, response);

cookieManager.setRememberMe("token123");          // 1 year
cookieManager.setTheme("dark");                   // 1 year
cookieManager.setLastVisit(System.currentTimeMillis());
cookieManager.remove("rememberMe");

String theme = cookieManager.getThemeOrDefault("light");
boolean remembered = cookieManager.has("rememberMe");
```

### Cookie Security Defaults

Every cookie written by `ClientCookieManager` gets:

- `HttpOnly` - JavaScript cannot read it (blocks XSS token theft)
- `Path=/` - sent to the whole application
- Explicit `Max-Age` - no session-only surprises for preference cookies

## Which One to Use?

| Need | Choice |
| --- | --- |
| Login state, cart, CSRF | Session |
| Flash / one-time messages | Session |
| "Remember me" across weeks | Cookie |
| Theme / language preference | Cookie |
| Last visit timestamp | Cookie |

## Rules to Remember

1. Never trust a cookie for authorization - cookies can be edited by the
   client. Store auth state in the session, not in a cookie.
2. Keep `ClientUser` and `ClientShoppingCart` `Serializable`; sessions can be
   persisted or distributed.
3. Never store the raw password - store `ClientSecurityUtil.hashPassword(...)`
   output only.
4. Always validate the CSRF token on state-changing POSTs.
5. Combine with `web.xml` / `@WebServlet` security constraints for real
   protection (see `STATE 5/implementation/SecurityBasicsServlet.java`).
