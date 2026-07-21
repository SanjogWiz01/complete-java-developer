# 04 - REST Principles Game

## Game Name: REST Principles Game

## What You Learn
- Statelessness principle
- Resource-based URI design
- JSON vs XML data formats
- REST design best practices

## Key Concepts

### 1. Statelessness
Each request must contain ALL information the server needs. The server does not remember previous requests.

**Why?**
- Easy to scale (any server instance can handle any request)
- Reliable (no session data lost if a server crashes)
- Simple server logic

**How is state maintained?** Client sends tokens (JWT, API key) with each request.

### 2. Resource-Based URIs
Everything is a resource. URIs identify resources, not actions.

| Good (Resource-Based) | Bad (Action-Based) |
|----------------------|-------------------|
| GET /api/users | GET /api/getUser?id=42 |
| POST /api/users | POST /api/createUser |
| DELETE /api/users/42 | POST /api/deleteUser?id=42 |

### 3. Data Formats

**JSON** (preferred for modern APIs):
```json
{"id": 1, "name": "Player One", "level": 5}
```

**XML** (used in SOAP/enterprise):
```xml
<user>
  <id>1</id>
  <name>Player One</name>
  <level>5</level>
</user>
```

### 4. Best Practices
1. Use plural nouns: `/api/users` not `/api/user`
2. Nest resources: `/api/users/42/orders`
3. Use proper HTTP methods
4. Return proper status codes
5. Support filtering: `/api/users?role=admin`
6. Version your API: `/api/v1/users`

## Run This File
```bash
javac 04_RestPrinciplesGame.java
java RestPrinciplesGame
```

## Next Topic
[05 - Spring Boot Arena](05_SpringBootArena.md) - Building APIs with Spring Boot
