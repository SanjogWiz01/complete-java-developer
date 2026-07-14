import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/architecture")
public class ServletArchitectureDemo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Servlet Architecture</title></head>");
        out.println("<body>");
        out.println("<h1>Servlet Architecture Demo</h1>");

        out.println("<h2>Architecture Flow</h2>");
        out.println("<p><b>Request -> Web Server -> Container -> Servlet -> Container -> Web Server -> Client</b></p>");

        out.println("<h2>Request Headers Received:</h2>");
        out.println("<table border='1' cellpadding='6'>");
        out.println("<tr><th>Header Name</th><th>Header Value</th></tr>");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            out.println("<tr><td>" + name + "</td><td>" + value + "</td></tr>");
        }
        out.println("</table>");

        out.println("<h2>Architecture Components:</h2>");
        out.println("<ul>");
        out.println("<li><b>Client:</b> Browser or API consumer</li>");
        out.println("<li><b>Web Server:</b> Accepts HTTP traffic</li>");
        out.println("<li><b>Servlet Container:</b> Runs servlet classes</li>");
        out.println("<li><b>Servlet:</b> Java class that processes requests</li>");
        out.println("<li><b>Response:</b> HTML, JSON, text, or redirect output</li>");
        out.println("</ul>");
        out.println("</body></html>");
    }
}
