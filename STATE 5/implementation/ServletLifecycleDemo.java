import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/servlet-lifecycle")
public class ServletLifecycleDemo extends HttpServlet {

    private int initCount = 0;
    private int serviceCount = 0;
    private String initializedAt;

    @Override
    public void init() throws ServletException {
        initCount++;
        initializedAt = java.time.LocalTime.now().toString();
        System.out.println("ServletLifecycleDemo: init() called. Instance #" + initCount);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        serviceCount++;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Servlet Lifecycle</title></head>");
        out.println("<body>");
        out.println("<h1>Servlet Lifecycle Demo</h1>");

        out.println("<h2>Lifecycle Status:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><td><b>Initialized At</b></td><td>" + initializedAt + "</td></tr>");
        out.println("<tr><td><b>Init Count</b></td><td>" + initCount + " (called once)</td></tr>");
        out.println("<tr><td><b>Service Call Count</b></td><td>" + serviceCount + " (called per request)</td></tr>");
        out.println("</table>");

        out.println("<h2>Lifecycle Methods:</h2>");
        out.println("<ul>");
        out.println("<li><b>init()</b> - Called once when the servlet is first created by the container</li>");
        out.println("<li><b>service()</b> - Called for every incoming request (routes to doGet, doPost, etc.)</li>");
        out.println("<li><b>destroy()</b> - Called once before the servlet is removed from service</li>");
        out.println("</ul>");

        out.println("<h2>Important Behavior:</h2>");
        out.println("<ul>");
        out.println("<li>Container creates ONE servlet instance</li>");
        out.println("<li>Multiple threads handle requests using the same instance</li>");
        out.println("<li>Avoid storing user-specific data in instance variables</li>");
        out.println("<li>Use method-local variables for request-specific data</li>");
        out.println("</ul>");

        out.println("<p><a href='servlet-lifecycle'>Refresh to see service count increase</a></p>");
        out.println("</body></html>");
    }

    @Override
    public void destroy() {
        System.out.println("ServletLifecycleDemo: destroy() called.");
    }
}
