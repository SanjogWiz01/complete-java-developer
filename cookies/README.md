# Cookies & Sessions in Java (Java 8/9)

Cookie creation, cookie implementation, cookie servers and session tracking - all runnable with a plain JDK, no external libraries.

## Files

| File | What it teaches | Run |
|------|-----------------|-----|
| `CookieCreationDemo.java` | How a cookie is born: name=value, Max-Age, Expires, Path, Domain, Secure, HttpOnly, SameSite; deleting cookies; parsing the browser's `Cookie:` header | `java CookieCreationDemo` |
| `CookieImplementation.java` | Reusable utility: build / parse / encode / delete cookies (used by both servers) | `java CookieImplementation` (self-test) |
| `CookieServer.java` | Real HTTP server that sets, reads, counts and deletes cookies. Stateless - everything lives in the browser | `javac CookieImplementation.java CookieServer.java` then `java CookieServer` -> http://localhost:8085 |
| `SessionManager.java` | Server-side sessions: thread-safe store, UUID session ids, idle timeout like Tomcat's 30 min | `java SessionManager` (demo) |
| `SessionServer.java` | Login/cart/logout flow: browser keeps only an opaque `JSESSIONID`; real state stays on the server; expired-session janitor thread | `javac CookieImplementation.java SessionManager.java SessionServer.java` then `java SessionServer` -> http://localhost:8086 |
| `CookieSessionServlet.java` | The same concepts with classic `javax.servlet.*` APIs for Tomcat 8/9 (`@WebServlet`, `request.getCookies()`, `HttpSession`) | deploy to a Servlet 3.0+ webapp |

## Cookie vs Session in one table

| | Cookie | Session |
|--|--------|---------|
| Stored where | Browser | Server memory |
| Travels over wire | Every request (full data) | Only an ID |
| Capacity | ~4 KB | Server-limited |
| Security | Visible/tamperable client-side | Opaque ID only |
| Lifetime | `Max-Age`/`Expires` | Idle timeout / logout |
| Typical use | Theme, remember-me, tracking | Login state, shopping cart |

## Quick tour

```bash
# terminal 1
java CookieServer
# terminal 2
curl -i "http://localhost:8085/set?name=theme&value=dark"   # Set-Cookie in response
curl -i "http://localhost:8085/"                            # send the cookie back
```

All code targets Java 8 lambdas/streams/Optional and also runs on Java 9+.
