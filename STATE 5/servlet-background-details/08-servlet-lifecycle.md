# Servlet Lifecycle

The servlet lifecycle is controlled by the servlet container.

## Lifecycle Methods

- `init`: Called once when the servlet is created.
- `service`: Called for every request.
- `destroy`: Called before the servlet is removed from service.

## Important Behavior

The container usually creates one servlet instance and uses multiple threads to handle requests.

Because of this, servlet instance fields must be used carefully. Request-specific data should stay inside method-local variables.

## Safe Practice

Avoid storing user-specific data in servlet instance variables.
