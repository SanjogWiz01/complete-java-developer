# Using Client Files Inside JSP

JSP is the view layer of the application. The client beans in this folder are
designed to be consumed directly from JSP pages using standard actions and
Expression Language (EL).

## `ClientUser` as a Bean

```jsp
<jsp:useBean id="user" class="ClientUser" scope="session" />

Hello, <b>${user.displayName}</b>!
Role: ${user.role}

<c:if test="${user.admin}">
    <a href="admin/dashboard">Admin Dashboard</a>
</c:if>
```

The `getDisplayName()` and `isAdmin()` methods map to the EL properties
`${user.displayName}` and `${user.admin}` automatically.

## `ClientResponse` as View Data

Servlets forward a `ClientResponse` as a request attribute:

```java
request.setAttribute("viewData", viewData);
request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
```

JSP reads it with JSTL:

```jsp
<c:if test="${viewData.data.flash != null}">
    <div class="alert">${viewData.data.flash}</div>
</c:if>

User: ${viewData.data.user.username}
Cart items: ${viewData.data.cartItemCount}
```

## `ClientShoppingCart` with JSTL

```jsp
<c:forEach var="item" items="${sessionScope.sessionCart.items}">
    <tr>
        <td>${item.value.name}</td>
        <td>${item.value.quantity}</td>
        <td>${item.value.lineTotal}</td>
    </tr>
</c:forEach>

Total: <b>${sessionScope.sessionCart.total}</b>
```

## Important Rules

1. Every class used in a JSP must be a JavaBean: a no-argument constructor and
   public getters/setters.
2. `ClientRequest` and `ClientResponse` are request-scoped DTOs - keep them out
   of the session.
3. `ClientUser` and `ClientShoppingCart` are `Serializable` so they survive
   session serialization.
4. Never call `ClientSecurityUtil.hashPassword()` or DB code from a JSP;
   keep logic in the servlet.
