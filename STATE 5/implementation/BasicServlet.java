import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/basic")
public class BasicServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Basic Servlet Program</title></head>");
        out.println("<body>");
        out.println("<h1>Basic Servlet Program</h1>");
        out.println("<p>This servlet demonstrates the fundamental structure of a Java Servlet.</p>");

        out.println("<h2>What this servlet does:</h2>");
        out.println("<ol>");
        out.println("<li>Extends HttpServlet class</li>");
        out.println("<li>Overrides doGet method</li>");
        out.println("<li>Uses HttpServletRequest to read request data</li>");
        out.println("<li>Uses HttpServletResponse to write output</li>");
        out.println("<li>Sets content type to text/html</li>");
        out.println("</ol>");

        out.println("<h2>Prerequisites for Running Servlets:</h2>");
        out.println("<ul>");
        out.println("<li>JDK (Java Development Kit) installed</li>");
        out.println("<li>Apache Tomcat server installed (version 9 or 10)</li>");
        out.println("<li>IDE like Eclipse or IntelliJ IDEA</li>");
        out.println("<li>Servlet API JAR (included with Tomcat)</li>");
        out.println("</ul>");

        out.println("<h2>Request Info:</h2>");
        out.println("<p>Method: " + request.getMethod() + "</p>");
        out.println("<p>URI: " + request.getRequestURI() + "</p>");
        out.println("<p>Query String: " + (request.getQueryString() != null ? request.getQueryString() : "None") + "</p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Basic Servlet - POST</title></head>");
        out.println("<body>");
        out.println("<h1>POST Request Received</h1>");
        out.println("<p>This servlet also handles POST requests via doPost method.</p>");
        out.println("</body></html>");
    }
}
