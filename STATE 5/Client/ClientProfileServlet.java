import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/profile")
public class ClientProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClientSessionManager sessionManager = new ClientSessionManager(request);

        if (!sessionManager.isLoggedIn()) {
            sessionManager.flashMessage("warning", "Please login to view your profile");
            response.sendRedirect("login");
            return;
        }

        ClientUser loggedInUser = sessionManager.getLoggedInUser();
        ClientUser freshUser = ClientRegistrationServlet.findUser(loggedInUser.getUsername());
        ClientUser user = freshUser != null ? freshUser : loggedInUser;

        ClientResponse viewData = ClientResponse.ok("Profile loaded");
        viewData.addData("user", user);
        viewData.addData("cartItemCount", sessionManager.getCart().getItemCount());
        viewData.addData("theme", new ClientCookieManager(request, response).getThemeOrDefault("light"));

        String flash = sessionManager.consumeFlashMessage("success");
        if (flash != null) {
            viewData.addData("flash", flash);
        }

        request.setAttribute("viewData", viewData);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClientSessionManager sessionManager = new ClientSessionManager(request);

        if (!sessionManager.isLoggedIn()) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if ("updateProfile".equals(action)) {
            ClientUser user = sessionManager.getLoggedInUser();
            user.setFullName(ClientSecurityUtil.sanitizeInput(request.getParameter("fullName")));
            user.setEmail(ClientSecurityUtil.trimToNull(request.getParameter("email")));
            sessionManager.flashMessage("success", "Profile updated");
        }

        response.sendRedirect("profile");
    }
}
