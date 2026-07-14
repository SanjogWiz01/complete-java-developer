# HTTP Method Handling

Servlets handle HTTP methods by overriding method-specific functions from `HttpServlet`.

## Common Methods

- `doGet`: Read or display data.
- `doPost`: Submit forms or create data.
- `doPut`: Update existing data.
- `doDelete`: Delete existing data.

## Practical Rule

Use `GET` for safe reads and `POST` for operations that change server-side state.

## Form Example

```html
<form method="post" action="/login">
    <input name="username">
    <input name="password" type="password">
    <button type="submit">Login</button>
</form>
```

The servlet mapped to `/login` can process this data inside `doPost`.
