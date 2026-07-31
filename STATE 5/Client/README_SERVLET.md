# Using Client Files Inside Servlets

Servlets act as controllers. They receive the browser request, use the client
helper classes to process it, and hand a `ClientResponse` to the JSP for
rendering.

## 1. Wrap Incoming Form Data

`ClientRequest` centralizes parameter access so servlets never touch raw
`request.getParameter()` calls repeatedly.

```java
ClientRequest clientRequest = ClientRequest.from(request);

String username = clientRequest.getField("username");
boolean filled = clientRequest.hasField("email");
```

## 2. Validate Before Acting

```java
ClientFormValidator validator = new ClientFormValidator();
validator.validateUsername("username", clientRequest.getField("username"));
validator.validateEmail("email", clientRequest.getField("email"));

if (validator.hasErrors()) {
    request.setAttribute("errors", validator.getErrors());
    request.setAttribute("formData", clientRequest.getFields());
    request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    return;
}
```

## 3. Manage Login State

`ClientSessionManager` wraps `HttpSession` so controllers have a clean API.

```java
ClientSessionManager sessionManager = new ClientSessionManager(request);

if (!sessionManager.isLoggedIn()) {
    response.sendRedirect("login");
    return;
}
ClientUser user = sessionManager.getLoggedInUser();
```

## 4. Handle Client State With Cookies

```java
ClientCookieManager cookieManager = new ClientCookieManager(request, response);
cookieManager.setTheme("dark");
String theme = cookieManager.getThemeOrDefault("light");
```

## 5. Secure the Data

```java
String hash = ClientSecurityUtil.hashPassword(rawPassword);
String escaped = ClientSecurityUtil.escapeHtml(userInput);
String token = ClientSecurityUtil.generateCsrfToken();
sessionManager.setCsrfToken(token);
```

## 6. Send Results to the View

```java
ClientResponse viewData = ClientResponse.ok("Profile loaded");
viewData.addData("user", user);
viewData.addData("cartItemCount", sessionManager.getCart().getItemCount());

request.setAttribute("viewData", viewData);
request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
```

## Pattern Summary

| Concern | Client class used |
| --- | --- |
| Read form data | `ClientRequest` |
| Validate fields | `ClientFormValidator` |
| Track login / cart | `ClientSessionManager` |
| Persistent preferences | `ClientCookieManager` |
| Cart totals | `ClientShoppingCart` |
| Hashing / escaping / CSRF | `ClientSecurityUtil` |
| Build view model | `ClientResponse` |
| Login / register / profile entry points | `ClientRegistrationServlet`, `ClientProfileServlet` |
