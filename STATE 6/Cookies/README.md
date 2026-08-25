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
| `SignedCookieUtil.java` | HMAC-SHA256 signed cookie values - client edits break the signature; constant-time verification | `java SignedCookieUtil` (self-test) |
| `CookieJar.java` | The browser's side of the deal: store Set-Cookie per domain/path, honour Max-Age/Expires, assemble `Cookie:` headers | `java CookieJar` (self-test) |
| `SessionIdGenerator.java` | Why session ids need SecureRandom + 128-bit entropy; collision demo with weak ids | `java SessionIdGenerator` |
| `SessionFixationDemo.java` | Session fixation attack vs. defense: regenerate the id at every privilege change | `java SessionFixationDemo` |
| `CsrfTokenManager.java` | CSRF defenses: synchronizer token in the session + double-submit cookie pattern | `java CsrfTokenManager` |
| `RememberMeService.java` | Safe "remember me": rotating series:token cookies, only hashes stored, replay detection | `java RememberMeService` |
| `CookieConsentManager.java` | GDPR consent categories (necessary/preferences/analytics/marketing) with audit trail | `java CookieConsentManager` |
| `CookieScopeValidator.java` | Audits Set-Cookie headers: Domain widening, missing Secure/HttpOnly, SameSite=None, `__Host-` rules | `java CookieScopeValidator` |
| `SessionPersistence.java` | Survive restarts: serialize sessions to disk, reload them, drop expired ones | `java SessionPersistence` |
| `CookieAttackLab.java` | Guided tour of hijacking/tampering/XSS-theft/CSRF/fixation and the attribute that stops each | `java CookieAttackLab` |

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
