# Unit 7 Capstone: Real-Life Inventory REST Application

This capstone combines the unit concepts:

- ORM concepts
- Hibernate/JPA mapping
- web framework architecture
- Spring Boot REST API
- concurrency with executor services
- Factory pattern
- Singleton concept

## Architecture

Client
→ REST Controller
→ Service
→ Repository/ORM
→ Database

Payment processing can use a Factory, while background order processing can use a bounded executor.

## Production improvements
For a production system, add:
- PostgreSQL/MySQL
- Spring Data JPA
- validation
- global exception handling
- authentication/authorization
- database migrations
- structured logging
- tests
- API documentation
- observability
- pagination
- transaction boundaries
- optimistic locking where appropriate

## Endpoints
- GET `/api/products`
- POST `/api/products`
- DELETE `/api/products/{id}`

## Learning objective
After completing this unit, you should be able to explain not just the syntax but how these technologies fit together in a real backend application.
