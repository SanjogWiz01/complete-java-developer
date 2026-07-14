import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/container-services")
public class ContainerServicesServlet extends HttpServlet {

    private int requestCount = 0;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        synchronized (this) {
            requestCount++;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Servlet Container Services</title></head>");
        out.println("<body>");
        out.println("<h1>Servlet Container Services</h1>");

        out.println("<h2>Container Information:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><td><b>Servlet Name</b></td><td>" + getServletName() + "</td></tr>");
        out.println("<tr><td><b>Servlet Context</b></td><td>" + getServletContext().getServletContextName() + "</td></tr>");
        out.println("<tr><td><b>Server Info</b></td><td>" + getServletContext().getServerInfo() + "</td></tr>");
        out.println("<tr><td><b>Context Path</b></td><td>" + request.getContextPath() + "</td></tr>");
        out.println("<tr><td><b>Total Requests Handled</b></td><td>" + requestCount + "</td></tr>");
        out.println("</table>");

        out.println("<h2>Services Provided by Container:</h2>");
        out.println("<ul>");
        out.println("<li>Network Services - loads servlet classes from filesystem or network</li>");
        out.println("<li>MIME Encoding/Decoding - handles MIME-based messages</li>");
        out.println("<li>Lifecycle Management - manages init, service, destroy</li>");
        out.println("<li>Resource Management - manages HTML, Servlets, JSP pages</li>");
        out.println("<li>Security Service - handles authorization and authentication</li>");
        out.println("<li>Session Management - maintains sessions via cookies or URL rewriting</li>");
        out.println("<li>Threading - handles concurrent requests with threads</li>");
        out.println("</ul>");
        out.println("</body></html>");
    }
}
