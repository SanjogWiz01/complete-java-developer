# Heavy Java Project - Order Management API

Production-style Spring Boot API with realistic business logic:

- Customers and products management
- Order creation with multi-item support
- Stock validation and decrement in one transaction
- Structured API error responses
- In-memory H2 DB for quick execution

## Tech

- Java 17
- Spring Boot 3
- Spring Data JPA + Hibernate
- Bean Validation
- H2 database
- Maven

## Run

```bash
mvn spring-boot:run
```

Base URL: `http://localhost:8080`

## Main APIs

- `POST /api/customers`
- `GET /api/customers`
- `POST /api/products`
- `GET /api/products`
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders/{id}`

## Example Order Request

```json
{
  "customerId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 3 }
  ]
}
```

