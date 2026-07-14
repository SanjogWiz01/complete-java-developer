import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/session")
public class SessionManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) {
            visitCount = 0;
        }
        visitCount++;
        session.setAttribute("visitCount", visitCount);

        String username = (String) session.getAttribute("username");
        String sessionId = session.getId();
        long createdTime = session.getCreationTime();
        long lastAccessedTime = session.getLastAccessedTime();
        int maxInactiveInterval = session.getMaxInactiveInterval();

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Session Management</title></head>");
        out.println("<body>");
        out.println("<h1>Session Management Demo</h1>");

        out.println("<h2>Session Info:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><td><b>Session ID</b></td><td>" + sessionId + "</td></tr>");
        out.println("<tr><td><b>Visit Count</b></td><td>" + visitCount + "</td></tr>");
        out.println("<tr><td><b>Username</b></td><td>" + (username != null ? username : "Not set") + "</td></tr>");
        out.println("<tr><td><b>Created At</b></td><td>" + new java.util.Date(createdTime) + "</td></tr>");
        out.println("<tr><td><b>Last Accessed</b></td><td>" + new java.util.Date(lastAccessedTime) + "</td></tr>");
        out.println("<tr><td><b>Max Inactive Interval</b></td><td>" + maxInactiveInterval + " seconds</td></tr>");
        out.println("</table>");

        out.println("<h2>Set Username in Session:</h2>");
        out.println("<form method='post' action='session'>");
        out.println("<input type='text' name='username' placeholder='Enter username'>");
        out.println("<button type='submit'>Set Username</button>");
        out.println("</form>");

        out.println("<h2>Session Techniques:</h2>");
        out.println("<ul>");
        out.println("<li>Cookies</li>");
        out.println("<li>URL Rewriting</li>");
        out.println("<li>Hidden Form Fields</li>");
        out.println("<li>HttpSession API</li>");
        out.println("</ul>");

        out.println("<h2>Common Uses:</h2>");
        out.println("<ul>");
        out.println("<li>Login state</li>");
        out.println("<li>Shopping cart data</li>");
        out.println("<li>User preferences</li>");
        out.println("<li>Multi-step form progress</li>");
        out.println("</ul>");

        out.println("<p><a href='session'>Refresh (count will increase)</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String username = request.getParameter("username");
        if (username != null && !username.trim().isEmpty()) {
            session.setAttribute("username", username.trim());
        }
        response.sendRedirect("session");
    }
}
