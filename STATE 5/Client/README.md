# STATE 5 / Client

Client-side Java files used by the Servlets and JSP layer of a Jakarta EE web
application. These classes model the browser side of the request-response
cycle: form data, validation, session state, cookies, the shopping cart, and
security helpers, plus two client-facing servlet controllers.

## File Overview

| File | Purpose |
| --- | --- |
| `ClientUser.java` | JavaBean representing the logged-in client user; used in JSP via `<jsp:useBean>` / EL |
| `ClientRequest.java` | DTO that wraps all submitted form parameters in a single object |
| `ClientResponse.java` | DTO that carries status, message, and view data from servlet to JSP |
| `ClientFormValidator.java` | Reusable client-side form validation (email, password, phone, length) |
| `ClientSessionManager.java` | Wraps `HttpSession` for login, cart, CSRF tokens, and flash messages |
| `ClientCookieManager.java` | Wraps `Cookie` handling for remember-me, theme, and last-visit tracking |
| `ClientShoppingCart.java` | Serializable cart bean with line totals; persists in the session |
| `ClientSecurityUtil.java` | HTML escaping, input sanitizing, SHA-256 hashing, CSRF token generation |
| `ClientRegistrationServlet.java` | `/register` controller: validates, creates the user, logs them in |
| `ClientProfileServlet.java` | `/profile` protected controller: guards the page and builds the JSP view data |

## Request Flow

```
Browser (JSP form)
      |  POST /register
      v
ClientRegistrationServlet
      |  ClientRequest.from(request)
      |  ClientFormValidator.validate*
      |  ClientSecurityUtil.hashPassword()
      v
ClientUser  --------->  stored + put into session via ClientSessionManager
      |
      v
ClientResponse  ----->  set as request attribute -> forward to JSP
```

## Related Folders

- `STATE 5/implementation` - core servlet programs (lifecycle, sessions, MIME, security)
- `STATE 5/servlet-background-details` - step-by-step servlet theory notes
- `STATE 5/Server` - server-side services and SOLID principle examples
