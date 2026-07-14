import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/http-methods")
public class HttpMethodServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>HTTP Method Handling</title></head>");
        out.println("<body>");
        out.println("<h1>HTTP Method Handling</h1>");
        out.println("<p>This servlet handles multiple HTTP methods.</p>");

        out.println("<h2>Common HTTP Methods:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><th>Method</th><th>Purpose</th><th>Servlet Override</th></tr>");
        out.println("<tr><td>GET</td><td>Read or display data</td><td>doGet()</td></tr>");
        out.println("<tr><td>POST</td><td>Submit forms or create data</td><td>doPost()</td></tr>");
        out.println("<tr><td>PUT</td><td>Update existing data</td><td>doPut()</td></tr>");
        out.println("<tr><td>DELETE</td><td>Delete existing data</td><td>doDelete()</td></tr>");
        out.println("</table>");

        out.println("<h2>Try a POST Request:</h2>");
        out.println("<form method='post' action='http-methods'>");
        out.println("<label>Username: <input type='text' name='username'></label><br><br>");
        out.println("<label>Password: <input type='password' name='password'></label><br><br>");
        out.println("<button type='submit'>Submit POST</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>POST Handled</title></head>");
        out.println("<body>");
        out.println("<h1>POST Request Processed</h1>");
        out.println("<p>Received username: <b>" + (username != null ? username : "Not provided") + "</b></p>");
        out.println("<p>POST method is used for operations that change server-side state.</p>");
        out.println("<a href='http-methods'>Back to GET</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.getWriter().println("PUT method received. Used for updating existing data.");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.getWriter().println("DELETE method received. Used for deleting existing data.");
    }
}
