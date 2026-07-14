# Introduction to APIs in Java

An API (Application Programming Interface) allows different software systems to communicate with each other.

In Java, APIs are used to connect to external services, exchange data, and build integrations between applications.

## What is an API?

An API defines the rules and protocols for how software components interact. It acts as a contract between the client and the server.

## Types of APIs in Java

- **REST API** - Uses HTTP methods, most common for web services
- **SOAP API** - XML-based, older protocol
- **GraphQL** - Query language for APIs
- **WebSocket** - Real-time bidirectional communication

## Key Concepts

- REST APIs use HTTP methods (GET, POST, PUT, DELETE) to perform operations.
- JSON is the most common data format for API communication.
- `HttpURLConnection` is the built-in Java class for making API calls.
- Libraries like Gson, Jackson, and OkHttp simplify API work.

## API Communication Flow

```
Client (Java app) -> HTTP Request -> API Server -> HTTP Response -> Client
```

## Common Use Cases

- Fetching weather data from a public API.
- Sending data to a payment gateway.
- Connecting to a database through a REST service.
- Building microservices that communicate with each other.
- Authenticating users via token-based systems.
