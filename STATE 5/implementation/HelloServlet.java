import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Hello Servlet</title></head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Welcome to Java Servlets</h1>");
        response.getWriter().println("<p>This is a basic servlet running inside a servlet container.</p>");
        response.getWriter().println("<p>Servlets run on the server side and handle HTTP requests.</p>");
        response.getWriter().println("<ul>");
        response.getWriter().println("<li>Runs inside a servlet container</li>");
        response.getWriter().println("<li>Handles HTTP requests and responses</li>");
        response.getWriter().println("<li>Supports enterprise web application development</li>");
        response.getWriter().println("</ul>");
        response.getWriter().println("</body></html>");
    }
}
