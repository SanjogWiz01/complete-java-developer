import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dynamic")
public class DynamicContentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = "Guest";
        }

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Dynamic Content</title></head>");
        out.println("<body>");
        out.println("<h1>Server-Side Dynamic Content</h1>");

        out.println("<h2>Hello, " + name + "!</h2>");
        out.println("<p>This page is generated dynamically by a servlet.</p>");
        out.println("<p>Current Server Time: <b>" + currentTime + "</b></p>");

        out.println("<h2>Static vs Dynamic:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><th>Static Page</th><th>Dynamic Servlet</th></tr>");
        out.println("<tr><td>Same content every time</td><td>Output changes based on input</td></tr>");
        out.println("<tr><td>HTML, CSS, JS files</td><td>Can read databases and sessions</td></tr>");
        out.println("<tr><td>No processing needed</td><td>Applies validation and business rules</td></tr>");
        out.println("</table>");

        out.println("<h2>Try Different Names:</h2>");
        out.println("<ul>");
        out.println("<li><a href='dynamic?name=Sanjog'>Sanjog</a></li>");
        out.println("<li><a href='dynamic?name=Java'>Java</a></li>");
        out.println("<li><a href='dynamic?name=Developer'>Developer</a></li>");
        out.println("</ul>");
        out.println("</body></html>");
    }
}
