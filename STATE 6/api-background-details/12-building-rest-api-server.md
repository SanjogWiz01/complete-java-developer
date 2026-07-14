# Building a REST API Server in Java

Java can also serve as a REST API server, not just a client.

## Using HttpServlet to Build Endpoints

```java
@WebServlet("/api/users")
public class UserApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.getWriter().println("{\"users\": [{\"name\": \"Sanjog\"}]}");
    }
}
```

## Using JAX-RS (Jakarta RESTful Web Services)

```java
@Path("/users")
public class UserResource {

    @GET
    @Produces("application/json")
    public Response getUsers() {
        return Response.ok(userList).build();
    }

    @POST
    @Consumes("application/json")
    public Response createUser(User user) {
        return Response.status(201).entity(user).build();
    }
}
```

## Using Spring Boot

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }
}
```

## Server-Side vs Client-Side

| Feature | Client | Server |
|---------|--------|--------|
| Role | Makes requests | Handles requests |
| Main Class | HttpURLConnection | HttpServlet / Controller |
| Purpose | Consume data | Provide data |
| Methods | GET, POST, PUT, DELETE | doGet, doPost, doPut, doDelete |

## Key Points

- Java is both a capable API client and server.
- HttpServlet handles the request-response cycle.
- JAX-RS provides a annotation-based approach.
- Spring Boot is the most popular framework for building REST APIs.
