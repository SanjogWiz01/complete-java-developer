# Servlet Architecture

Servlet architecture is based on a container-managed model.

The developer writes servlet classes, but the container controls loading, object creation, lifecycle calls, threading, and request dispatching.

## Main Parts

- Client: Browser or API consumer.
- Web server: Accepts HTTP traffic.
- Servlet container: Runs servlet classes.
- Servlet: Java class that processes requests.
- Response: HTML, JSON, text, file, or redirect output.

## Architecture Flow

Request -> Web server -> Container -> Servlet -> Container -> Web server -> Client

This separation allows Java developers to focus on application logic instead of low-level networking.
