# REST API Concepts

REST (Representational State Transfer) is an architectural style for designing networked applications.

## REST Principles

- **Stateless** - Each request contains all information needed to process it.
- **Client-Server** - Separation of concerns between client and server.
- **Cacheable** - Responses can be cached to improve performance.
- **Uniform Interface** - Consistent way to interact with resources.

## HTTP Methods in REST

| Method | Purpose | Idempotent |
|--------|---------|------------|
| GET | Read data | Yes |
| POST | Create data | No |
| PUT | Update/replace data | Yes |
| PATCH | Partial update | No |
| DELETE | Remove data | Yes |

## Resource Naming

Resources are identified by URLs:
- `/api/users` - Collection of users
- `/api/users/1` - Specific user with ID 1
- `/api/users/1/orders` - Orders belonging to user 1

## Status Codes

- **200 OK** - Request successful
- **201 Created** - Resource created
- **400 Bad Request** - Invalid request
- **401 Unauthorized** - Authentication required
- **403 Forbidden** - Access denied
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

## REST API Example

```
GET https://api.example.com/users/1
Response: {"id": 1, "name": "Sanjog", "email": "sanjog@example.com"}
```
