import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/lifecycle")
public class RequestResponseLifecycleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Request Response Lifecycle</title></head>");
        out.println("<body>");
        out.println("<h1>Request Response Lifecycle</h1>");

        out.println("<h2>Request Data:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><td><b>Request URI</b></td><td>" + request.getRequestURI() + "</td></tr>");
        out.println("<tr><td><b>Method</b></td><td>" + request.getMethod() + "</td></tr>");
        out.println("<tr><td><b>Protocol</b></td><td>" + request.getProtocol() + "</td></tr>");
        out.println("<tr><td><b>Remote Address</b></td><td>" + request.getRemoteAddr() + "</td></tr>");
        out.println("<tr><td><b>Remote Host</b></td><td>" + request.getRemoteHost() + "</td></tr>");
        out.println("<tr><td><b>Server Name</b></td><td>" + request.getServerName() + "</td></tr>");
        out.println("<tr><td><b>Server Port</b></td><td>" + request.getServerPort() + "</td></tr>");
        out.println("</table>");

        out.println("<h2>Lifecycle Workflow:</h2>");
        out.println("<ol>");
        out.println("<li>Client sends HTTP request</li>");
        out.println("<li>Web server receives the request</li>");
        out.println("<li>Server forwards to servlet container</li>");
        out.println("<li>Container selects matching servlet</li>");
        out.println("<li>Servlet processes the request</li>");
        out.println("<li>Servlet writes output to response</li>");
        out.println("<li>Container sends response back to web server</li>");
        out.println("<li>Browser displays the result</li>");
        out.println("</ol>");
        out.println("</body></html>");
    }
}
