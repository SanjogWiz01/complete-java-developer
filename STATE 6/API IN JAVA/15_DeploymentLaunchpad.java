public class DeploymentLaunchpad {

    public static void main(String[] args) {
        System.out.println("=== DEPLOYMENT LAUNCHPAD: Deployment & Versioning ===\n");
        versioningStrategies();
        versioningInSpring();
        deploymentOptions();
        productionChecklist();
    }

    private static void versioningStrategies() {
        System.out.println("--- 1. API Versioning Strategies ---\n");
        System.out.println("When you change your API, old clients must not break.\n");

        System.out.println("Strategy 1: URI Versioning (Most Common)");
        System.out.println("  GET /api/v1/games");
        System.out.println("  GET /api/v2/games");
        System.out.println("  Pros: Clear, easy to route, visible in URLs.");
        System.out.println("  Cons: URIs proliferate.\n");

        System.out.println("Strategy 2: Header Versioning");
        System.out.println("  GET /api/games");
        System.out.println("  Header: X-API-Version: 1");
        System.out.println("  Pros: Clean URLs.");
        System.out.println("  Cons: Hidden from URL, harder to test in browser.\n");

        System.out.println("Strategy 3: Query Parameter Versioning");
        System.out.println("  GET /api/games?version=1");
        System.out.println("  Pros: Simple to implement.");
        System.out.println("  Cons: Pollutes URL, easily forgotten.\n");

        System.out.println("Strategy 4: Content Negotiation (Media Type Versioning)");
        System.out.println("  GET /api/games");
        System.out.println("  Header: Accept: application/vnd.myapi.v1+json");
        System.out.println("  Pros: RESTful, uses HTTP correctly.");
        System.out.println("  Cons: Complex to implement, hard to test.\n");

        System.out.println("Recommendation: Use URI versioning for simplicity and clarity.\n");
    }

    private static void versioningInSpring() {
        System.out.println("--- 2. Versioning Implementation in Spring Boot ---\n");

        System.out.println("Method 1: Multiple controller classes with different base paths");
        System.out.println("  @RestController");
        System.out.println("  @RequestMapping(\"/api/v1/games\")");
        System.out.println("  public class GameControllerV1 { ... }\n");
        System.out.println("  @RestController");
        System.out.println("  @RequestMapping(\"/api/v2/games\")");
        System.out.println("  public class GameControllerV2 { ... }\n");

        System.out.println("Method 2: Different packages");
        System.out.println("  com.example.api.v1.GameController");
        System.out.println("  com.example.api.v2.GameController\n");

        System.out.println("Method 3: RequestMapping with headers");
        System.out.println("  @RequestMapping(value = \"/api/games\", headers = \"X-API-Version=1\")");
        System.out.println("  public class GameControllerV1 { ... }\n");

        System.out.println("Deprecation policy:");
        System.out.println("  - Support old version for a grace period (e.g., 6-12 months).");
        System.out.println("  - Return deprecation headers: Deprecation: true, Sunset: Sat, 01 Jan 2025.");
        System.out.println("  - Notify clients before removing old version.\n");
    }

    private static void deploymentOptions() {
        System.out.println("--- 3. Deployment Options ---\n");

        System.out.println("Option 1: Traditional Server");
        System.out.println("  - Deploy WAR to Tomcat/JBoss/WildFly.");
        System.out.println("  - Build: mvn clean package");
        System.out.println("  - Copy target/*.war to server's webapps/ directory.\n");

        System.out.println("Option 2: Embedded Server (Spring Boot)");
        System.out.println("  - Build: mvn clean package");
        System.out.println("  - Run: java -jar target/app.jar");
        System.out.println("  - Spring Boot embeds Tomcat inside the JAR.\n");

        System.out.println("Option 3: Docker Container");
        System.out.println("  Dockerfile:");
        System.out.println("    FROM eclipse-temurin:17-jre-alpine");
        System.out.println("    COPY target/app.jar /app.jar");
        System.out.println("    EXPOSE 8080");
        System.out.println("    ENTRYPOINT [\"java\", \"-jar\", \"/app.jar\"]");
        System.out.println("  Build: docker build -t my-api .");
        System.out.println("  Run: docker run -p 8080:8080 my-api\n");

        System.out.println("Option 4: Cloud Platforms");
        System.out.println("  - AWS: Elastic Beanstalk, ECS, Lambda (serverless).");
        System.out.println("  - Google Cloud: Cloud Run, App Engine, GKE.");
        System.out.println("  - Azure: App Service, AKS, Azure Functions.");
        System.out.println("  - Heroku: git push heroku main (simplest).\n");

        System.out.println("Option 5: Kubernetes");
        System.out.println("  - Containerize with Docker.");
        System.out.println("  - Deploy to K8s cluster for auto-scaling, load balancing.");
        System.out.println("  - Use Helm charts for configuration management.\n");
    }

    private static void productionChecklist() {
        System.out.println("--- 4. Production Checklist ---\n");
        System.out.println("Before deploying to production:\n");
        System.out.println("  [ ] HTTPS enabled (SSL/TLS certificate).");
        System.out.println("  [ ] Environment variables for secrets (not hardcoded).");
        System.out.println("  [ ] Database connection pooling configured.");
        System.out.println("  [ ] Logging configured (SLF4J + Logback/Log4j2).");
        System.out.println("  [ ] Health check endpoint: /api/actuator/health");
        System.out.println("  [ ] Rate limiting implemented.");
        System.out.println("  [ ] CORS configured for allowed origins.");
        System.out.println("  [ ] API versioning strategy in place.");
        System.out.println("  [ ] Monitoring and alerting (Prometheus + Grafana).");
        System.out.println("  [ ] CI/CD pipeline for automated testing and deployment.");
        System.out.println("  [ ] Load testing done (JMeter, Gatling).");
        System.out.println("  [ ] Database backups configured.");
        System.out.println("  [ ] API documentation published (Swagger/OpenAPI).\n");

        System.out.println("=== End of Deployment Launchpad ===");
    }
}
