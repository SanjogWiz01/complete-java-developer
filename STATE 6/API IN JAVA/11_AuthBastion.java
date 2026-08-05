public class AuthBastion {

    public static void main(String[] args) {
        System.out.println("=== AUTH BASTION: Authentication & Authorization ===\n");
        authenticationVsAuthorization();
        basicAuth();
        jwtAuth();
        oauth2Overview();
    }

    private static void authenticationVsAuthorization() {
        System.out.println("--- Authentication vs Authorization ---\n");
        System.out.println("Authentication: WHO are you? (Verify identity)");
        System.out.println("Authorization:  WHAT can you do? (Verify permissions)\n");
        System.out.println("Example:");
        System.out.println("  Authentication: Logging in with username and password.");
        System.out.println("  Authorization:  An admin can delete users, a regular user cannot.\n");
    }

    private static void basicAuth() {
        System.out.println("--- 1. Basic Authentication ---\n");
        System.out.println("Client sends credentials in every request header.\n");

        System.out.println("Header format: Authorization: Basic base64(username:password)");
        System.out.println("Example: Authorization: Basic YWxpY2U6c2VjcmV0");
        System.out.println("  (decoded: alice:secret)\n");

        System.out.println("Spring Security config:");
        System.out.println("  @Bean");
        System.out.println("  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {");
        System.out.println("      http");
        System.out.println("          .csrf(csrf -> csrf.disable())");
        System.out.println("          .authorizeHttpRequests(auth -> auth");
        System.out.println("              .requestMatchers(\"/api/public/**\").permitAll()");
        System.out.println("              .anyRequest().authenticated())");
        System.out.println("          .httpBasic(Customizer.withDefaults());");
        System.out.println("      return http.build();");
        System.out.println("  }\n");

        System.out.println("Pros: Simple to implement.");
        System.out.println("Cons: Credentials sent with every request (use HTTPS!). Not stateless-friendly.\n");
    }

    private static void jwtAuth() {
        System.out.println("--- 2. JWT (JSON Web Token) Authentication ---\n");
        System.out.println("Stateless authentication using signed tokens.\n");

        System.out.println("Flow:");
        System.out.println("  1. Client sends username/password to /api/auth/login.");
        System.out.println("  2. Server validates credentials and generates a JWT.");
        System.out.println("  3. Client stores the JWT (localStorage or memory).");
        System.out.println("  4. Client sends JWT in Authorization header for every request:");
        System.out.println("     Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...");
        System.out.println("  5. Server validates the token signature and extracts user info.\n");

        System.out.println("JWT structure: header.payload.signature");
        System.out.println("  Header:  {\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        System.out.println("  Payload: {\"sub\":\"alice\",\"role\":\"ADMIN\",\"exp\":1700000000}");
        System.out.println("  Signature: HMAC-SHA256(header + payload, secret_key)\n");

        System.out.println("Java JWT library: JJWT (io.jsonwebtoken)");
        System.out.println("  String token = Jwts.builder()");
        System.out.println("      .setSubject(\"alice\")");
        System.out.println("      .claim(\"role\", \"ADMIN\")");
        System.out.println("      .setExpiration(new Date(System.currentTimeMillis() + 3600000))");
        System.out.println("      .signWith(SignatureAlgorithm.HS256, secretKey)");
        System.out.println("      .compact();\n");

        System.out.println("Pros: Stateless, scalable, no server-side session storage.");
        System.out.println("Cons: Token revocation is harder (use short expiration + refresh tokens).\n");
    }

    private static void oauth2Overview() {
        System.out.println("--- 3. OAuth2 Overview ---\n");
        System.out.println("OAuth2 lets users authorize third-party apps without sharing passwords.\n");

        System.out.println("Grant types:");
        System.out.println("  1. Authorization Code: Most secure. Used by web apps.");
        System.out.println("  2. Client Credentials: Machine-to-machine (no user involved).");
        System.out.println("  3. Password Grant: Direct login (deprecated, not recommended).");
        System.out.println("  4. PKCE: Authorization code + code verifier for mobile/SPA apps.\n");

        System.out.println("Flow (Authorization Code):");
        System.out.println("  1. User clicks 'Login with Google' on your app.");
        System.out.println("  2. Redirected to Google's authorization page.");
        System.out.println("  3. User approves, Google redirects back with an authorization code.");
        System.out.println("  4. Your server exchanges the code for an access token.");
        System.out.println("  5. Use the access token to call protected APIs.\n");

        System.out.println("Spring Security supports OAuth2 with:");
        System.out.println("  - spring-boot-starter-oauth2-client");
        System.out.println("  - spring-boot-starter-oauth2-resource-server\n");

        System.out.println("=== End of Auth Bastion ===");
    }
}
