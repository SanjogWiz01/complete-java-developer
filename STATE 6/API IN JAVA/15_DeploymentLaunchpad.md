# 15 - Deployment Launchpad: Deployment & Versioning

## Game Name: Deployment Launchpad

## What You Learn
- API versioning strategies
- Versioning implementation in Spring Boot
- Deployment options (server, Docker, cloud)
- Production checklist

## Key Concepts

### 1. Versioning Strategies

| Strategy | Example | Pros | Cons |
|----------|---------|------|------|
| URI | /api/v1/games | Clear, easy to route | URL proliferation |
| Header | X-API-Version: 1 | Clean URLs | Hidden, hard to test |
| Query Param | ?version=1 | Simple | Pollutes URL |
| Content Type | Accept: application/vnd.api.v1+json | RESTful | Complex |

**Recommendation:** Use URI versioning for simplicity.

### 2. Spring Boot Versioning
```java
@RestController
@RequestMapping("/api/v1/games")
public class GameControllerV1 { ... }

@RestController
@RequestMapping("/api/v2/games")
public class GameControllerV2 { ... }
```

### 3. Deployment Options

**Traditional Server:**
```bash
mvn clean package
# Copy target/*.war to server's webapps/
```

**Embedded Server:**
```bash
java -jar target/app.jar
```

**Docker:**
```dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/app.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

**Cloud Platforms:** AWS (ECS/Lambda), Google Cloud (Cloud Run), Azure (App Service), Heroku.

### 4. Production Checklist
- [ ] HTTPS enabled
- [ ] Environment variables for secrets
- [ ] Connection pooling configured
- [ ] Logging configured
- [ ] Health check endpoint (/actuator/health)
- [ ] Rate limiting implemented
- [ ] CORS configured
- [ ] API versioning in place
- [ ] Monitoring and alerting
- [ ] CI/CD pipeline
- [ ] Load testing done
- [ ] Database backups
- [ ] API docs published

## Run This File
```bash
javac 15_DeploymentLaunchpad.java
java DeploymentLaunchpad
```

## Congratulations! You have completed the Java API Journey!
