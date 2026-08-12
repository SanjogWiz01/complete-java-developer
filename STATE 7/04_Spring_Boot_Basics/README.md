# 7.4 Basics of Spring Boot

Spring Boot simplifies Spring application setup through convention, auto-configuration, starters and an embedded server model.

## Core concepts
- `@SpringBootApplication`: main Boot application annotation.
- IoC container: creates and manages beans.
- Dependency Injection: dependencies are supplied to classes.
- `@RestController`: controller returning response bodies.
- `@GetMapping`, `@PostMapping`, etc.: route HTTP requests.
- `@Service`: business logic layer.
- `@Repository`: persistence abstraction.
- `application.properties`: externalized configuration.

## Typical architecture
Controller → Service → Repository → Database.

## Real application
The sample is a REST API for a small inventory system. It demonstrates:
- GET all products
- GET product by ID
- POST product
- DELETE product
- layered design
- in-memory repository

## Run
Use Maven:
`mvn spring-boot:run`

Then open:
`GET http://localhost:8080/api/products`
