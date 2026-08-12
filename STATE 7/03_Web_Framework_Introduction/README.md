# 7.3 Web Framework Introduction

A Java web framework provides reusable infrastructure for HTTP request handling, routing, validation, serialization, sessions, security integration and view/API development.

## MVC
- Model: application data/domain.
- View: user-facing representation.
- Controller: handles requests and coordinates application behavior.

## HTTP request flow
Browser/client → server → router/controller → service → repository/database → response.

## REST
Common REST conventions:
- GET `/products` → list
- GET `/products/{id}` → one resource
- POST `/products` → create
- PUT `/products/{id}` → replace/update
- DELETE `/products/{id}` → delete

## Java ecosystem
Servlet-based frameworks include Spring MVC. Spring Web MVC is built on the Servlet API and uses components such as `DispatcherServlet` and annotated controllers.

## Real application
The sample `SimpleWebServer.java` is a dependency-free learning server using Java's built-in HTTP server. It demonstrates the basic request/response lifecycle before moving to Spring Boot.
