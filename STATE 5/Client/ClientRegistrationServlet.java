import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class ClientRegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ClientUser> USERS = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "register");
        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClientRequest clientRequest = ClientRequest.from(request);
        ClientFormValidator validator = new ClientFormValidator();

        validator.validateUsername("username", clientRequest.getField("username"));
        validator.validateEmail("email", clientRequest.getField("email"));
        validator.validatePassword("password", clientRequest.getField("password"));
        validator.validatePasswordMatch(
                clientRequest.getField("password"),
                clientRequest.getField("confirmPassword"),
                "Password");
        validator.validateLength("fullName", clientRequest.getField("fullName"), 60, "Full name");

        String csrfToken = clientRequest.getField("csrfToken");
        ClientSessionManager sessionManager = new ClientSessionManager(request);
        if (!sessionManager.isValidCsrf(csrfToken)) {
            validator.getErrors().put("csrfToken", "Security token invalid. Refresh and retry.");
        }

        if (validator.hasErrors()) {
            request.setAttribute("errors", validator.getErrors());
            request.setAttribute("formData", clientRequest.getFields());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        String username = clientRequest.getField("username");
        String email = clientRequest.getField("email");

        if (USERS.containsKey(username)) {
            validator.getErrors().put("username", "Username already taken");
            request.setAttribute("errors", validator.getErrors());
            request.setAttribute("formData", clientRequest.getFields());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        ClientUser user = new ClientUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(ClientSecurityUtil.hashPassword(clientRequest.getField("password")));
        user.setFullName(clientRequest.getField("fullName"));
        user.setRole("USER");
        user.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date()));

        USERS.put(username, user);

        sessionManager.login(user);
        sessionManager.flashMessage("success", "Account created. Welcome, " + username + "!");

        response.sendRedirect("profile");
    }

    public static ClientUser findUser(String username) {
        return USERS.get(username);
    }

    public static Map<String, ClientUser> findAllUsers() {
        return USERS;
    }
}
