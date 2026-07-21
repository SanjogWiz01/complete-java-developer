# 11 - Auth Bastion: Authentication & Authorization

## Game Name: Auth Bastion

## What You Learn
- Difference between authentication and authorization
- Basic Authentication setup
- JWT (JSON Web Token) authentication
- OAuth2 overview

## Key Concepts

### Authentication vs Authorization
- **Authentication**: WHO are you? (Verify identity)
- **Authorization**: WHAT can you do? (Verify permissions)

### 1. Basic Authentication
Client sends credentials in every request:
```
Authorization: Basic base64(username:password)
```

**Spring Security config:**
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
}
```

**Pros:** Simple. **Cons:** Credentials sent every request (must use HTTPS).

### 2. JWT Authentication
Stateless authentication using signed tokens.

**Flow:**
1. Client sends credentials to `/api/auth/login`
2. Server validates and generates JWT
3. Client stores JWT and sends it in `Authorization: Bearer <token>`
4. Server validates token signature

**JWT structure:** `header.payload.signature`

**JJWT library:**
```java
String token = Jwts.builder()
    .setSubject("alice")
    .claim("role", "ADMIN")
    .setExpiration(new Date(System.currentTimeMillis() + 3600000))
    .signWith(SignatureAlgorithm.HS256, secretKey)
    .compact();
```

### 3. OAuth2
Third-party apps access resources without sharing passwords.

**Grant types:** Authorization Code, Client Credentials, PKCE.

## Run This File
```bash
javac 11_AuthBastion.java
java AuthBastion
```

## Next Topic
[12 - Swagger Chronicles](12_SwaggerChronicles.md) - API Documentation
