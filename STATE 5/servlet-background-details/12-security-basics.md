# Servlet Security Basics

Servlet applications often need authentication and authorization.

Authentication verifies who the user is. Authorization decides what the user can access.

## Common Security Areas

- Login validation.
- Role-based access.
- Session timeout.
- HTTPS.
- Input validation.
- Password hashing.

## Practical Notes

- Never store plain text passwords.
- Validate all request parameters.
- Do not trust client-side checks only.
- Regenerate sessions after login when possible.
- Restrict admin URLs to admin roles.

Security should be planned early because servlets directly process client input.
