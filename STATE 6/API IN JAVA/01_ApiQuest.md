# 01 - API Quest: Understanding API Basics

## Game Name: API Quest

## What You Learn
- What an API is and why it matters
- Types of APIs: REST, SOAP, GraphQL
- How Java interacts with APIs
- Client-server communication model

## Key Concepts

### What is an API?
An **Application Programming Interface (API)** is a contract between two software components. It defines how the client sends requests and how the server sends responses.

**Real-world analogy:** A restaurant waiter takes your order to the kitchen and brings back food. You don't need to know how the kitchen works.

### Types of APIs

| Type | Format | Use Case |
|------|--------|----------|
| REST | JSON/XML | Web and mobile apps (most popular) |
| SOAP | XML only | Enterprise/banking systems |
| GraphQL | JSON | Client specifies exact data needed |

### Java API Interaction Workflow
1. Build the request (URL, method, headers, body)
2. Send the request using an HTTP client
3. Receive the response
4. Parse the response (JSON/XML to Java objects)
5. Handle errors if status code is not 2xx

## Run This File
```bash
javac 01_ApiQuest.java
java ApiQuest
```

## Next Topic
[02 - HTTP Battleground](02_HttpBattleground.md) - HTTP Methods, Status Codes, and Headers
