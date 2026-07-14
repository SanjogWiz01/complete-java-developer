import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/security")
public class SecurityBasicsServlet extends HttpServlet {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD_HASH = hashPassword("admin123");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute("loggedIn") != null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Security Basics</title></head>");
        out.println("<body>");
        out.println("<h1>Servlet Security Basics</h1>");

        if (isLoggedIn) {
            out.println("<h2>Welcome, " + session.getAttribute("username") + "!</h2>");
            out.println("<p>You are logged in.</p>");
            out.println("<h2>Security Practices:</h2>");
            out.println("<ul>");
            out.println("<li>Never store plain text passwords</li>");
            out.println("<li>Validate all request parameters</li>");
            out.println("<li>Do not trust client-side checks only</li>");
            out.println("<li>Regenerate sessions after login</li>");
            out.println("<li>Restrict admin URLs to admin roles</li>");
            out.println("</ul>");
            out.println("<a href='security?action=logout'>Logout</a>");
        } else {
            String error = request.getParameter("error");
            if ("invalid".equals(error)) {
                out.println("<p style='color:red;'>Invalid username or password!</p>");
            }

            out.println("<h2>Login Form:</h2>");
            out.println("<form method='post' action='security'>");
            out.println("<label>Username: <input type='text' name='username' required></label><br><br>");
            out.println("<label>Password: <input type='password' name='password' required></label><br><br>");
            out.println("<button type='submit'>Login</button>");
            out.println("</form>");
            out.println("<p><i>Try: admin / admin123</i></p>");

            out.println("<h2>Common Security Areas:</h2>");
            out.println("<ul>");
            out.println("<li>Login validation</li>");
            out.println("<li>Role-based access</li>");
            out.println("<li>Session timeout</li>");
            out.println("<li>HTTPS</li>");
            out.println("<li>Input validation</li>");
            out.println("<li>Password hashing</li>");
            out.println("</ul>");
        }
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("security");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (VALID_USERNAME.equals(username) && VALID_PASSWORD_HASH.equals(hashPassword(password))) {
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedIn", true);
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(300);
            response.sendRedirect("security");
        } else {
            response.sendRedirect("security?error=invalid");
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
