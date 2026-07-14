# API Development Checklist

## Before Starting
- [ ] Understand the API documentation
- [ ] Know the authentication method required
- [ ] Test the API manually with Postman or cURL
- [ ] Identify the data format (JSON, XML, etc.)

## For API Client Code
- [ ] Use proper HTTP method for each operation
- [ ] Set correct headers (Content-Type, Authorization)
- [ ] Handle all response status codes
- [ ] Set connection and read timeouts
- [ ] Close streams and connections properly
- [ ] Handle exceptions gracefully
- [ ] Use try-with-resources for auto-cleanup
- [ ] Avoid hardcoding API keys

## For API Server Code
- [ ] Validate all incoming request data
- [ ] Return appropriate HTTP status codes
- [ ] Use correct Content-Type in responses
- [ ] Implement authentication and authorization
- [ ] Log requests and errors
- [ ] Handle edge cases and null values
- [ ] Use connection pooling for databases
- [ ] Implement rate limiting

## Security
- [ ] Always use HTTPS
- [ ] Never expose sensitive data in logs
- [ ] Sanitize user input
- [ ] Use environment variables for secrets
- [ ] Implement token expiration
