# 02 - HTTP Battleground: HTTP Fundamentals

## Game Name: HTTP Battleground

## What You Learn
- HTTP methods: GET, POST, PUT, DELETE, PATCH
- HTTP status codes and their meanings
- HTTP headers and their purposes

## Key Concepts

### HTTP Methods

| Method | Purpose | Idempotent |
|--------|---------|------------|
| GET | Retrieve data | Yes |
| POST | Create new resource | No |
| PUT | Replace entire resource | Yes |
| PATCH | Partially update resource | Depends |
| DELETE | Remove a resource | Yes |

### Common Status Codes

| Code | Meaning | When to Use |
|------|---------|-------------|
| 200 | OK | Successful GET, PUT, PATCH |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid client input |
| 401 | Unauthorized | Authentication failed |
| 403 | Forbidden | Not allowed to access |
| 404 | Not Found | Resource doesn't exist |
| 500 | Internal Server Error | Server crashed |

### Important Headers
- **Content-Type**: Format of request/response body
- **Accept**: Desired response format
- **Authorization**: Authentication credentials
- **Cache-Control**: Caching instructions

## Run This File
```bash
javac 02_HttpBattleground.java
java HttpBattleground
```

## Next Topic
[03 - HttpClient Forge](03_HttpClientForge.md) - Java HTTP Client Options
