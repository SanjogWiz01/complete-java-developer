# Client Form Handling and Validation

Forms are the primary way a browser sends data to a servlet. This page explains
how the client-side files turn a raw HTML form submission into validated,
safe data.

## The Full Cycle

```
1. User fills the HTML form in the JSP page
2. Browser submits a POST request to the servlet (@WebServlet URL)
3. Servlet wraps parameters with ClientRequest
4. Servlet validates with ClientFormValidator
5. If errors -> forward back to the JSP with the errors map
6. If valid  -> process data, log in, redirect (POST-redirect-GET)
```

## Example Registration Form

```html
<form method="post" action="register">
    <input type="hidden" name="action" value="register">
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

    <label>Username
        <input type="text" name="username" value="${formData.username}">
        <span class="error">${errors.username}</span>
    </label>

    <label>Email
        <input type="email" name="email" value="${formData.email}">
        <span class="error">${errors.email}</span>
    </label>

    <label>Password
        <input type="password" name="password">
        <span class="error">${errors.password}</span>
    </label>

    <label>Confirm Password
        <input type="password" name="confirmPassword">
        <span class="error">${errors.confirmPassword}</span>
    </label>

    <button type="submit">Create Account</button>
</form>
```

## What the Servlet Validates

```java
ClientFormValidator validator = new ClientFormValidator();
validator.validateUsername("username", clientRequest.getField("username"));
validator.validateEmail("email", clientRequest.getField("email"));
validator.validatePassword("password", clientRequest.getField("password"));
validator.validatePasswordMatch(password, confirm, "Password");
validator.validateLength("fullName", clientRequest.getField("fullName"), 60, "Full name");
validator.validatePhone("phone", clientRequest.getField("phone"));
```

Available checks:

| Method | Purpose |
| --- | --- |
| `validateRequired(name, value, label)` | Field must not be blank |
| `validateEmail(name, value)` | Regex check + required |
| `validateUsername(name, value)` | 3-20 letters/digits/underscores |
| `validatePassword(name, value)` | Minimum 6 characters |
| `validatePasswordMatch(value, confirm, label)` | Both fields equal |
| `validatePhone(name, value)` | Optional, but well-formed |
| `validateLength(name, value, max, label)` | Maximum length cap |

## Best Practices

1. Use POST for anything that changes server state; keep GET for reads.
2. Redirect after a successful POST (`response.sendRedirect`) so a browser
   refresh cannot resubmit the form.
3. Re-fill the form from `formData` and show `errors` inline, never silently
   clear user input.
4. Always pair forms with a CSRF token checked in the servlet
   (`sessionManager.isValidCsrf(...)`).
5. Escape every echoed value with `ClientSecurityUtil.escapeHtml(...)` to block
   stored XSS.
