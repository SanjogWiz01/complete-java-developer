# Java Web + Spring Boot Study Pack — 15 Java Files

This pack covers:

1. Cookies basics
2. Cookie reading/deletion/security
3. HttpSession basics
4. Login/logout with sessions
5. Session timeout and lifecycle
6. Cookies + sessions in Spring Boot MVC
7. Creational design patterns
8. Structural design patterns
9. Behavioral design patterns
10. Spring Boot layered architecture
11. Dependency Injection and beans
12. Spring Security + session management
13. Design patterns inside Spring Boot
14. Spring Session + Redis
15. Integrated Spring Boot web demo

## Important

The files are intentionally Java-focused. Configuration snippets are included as Java comments where properties or Maven dependencies are needed.

For a real Spring Boot project, place classes under `src/main/java` and add the appropriate Spring Boot starters.

## Recommended order

01 → 02 → 03 → 04 → 05 → 06 → 07 → 08 → 09 → 10 → 11 → 12 → 13 → 14 → 15

## Current framework notes

The examples use `jakarta.servlet.*`, matching modern Spring Boot / Jakarta-based applications.

For production applications:
- Use HTTPS.
- Use Secure and HttpOnly cookies where appropriate.
- Choose an appropriate SameSite policy.
- Do not put secrets/passwords in cookies.
- Validate all client-controlled cookie values.
- Use Spring Security for authentication instead of handwritten login code.
- Consider Spring Session + Redis for horizontally scaled deployments.

Official references:
- Spring Boot Servlet Web Applications
- Spring Session
- Spring Security
