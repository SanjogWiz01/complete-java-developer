import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/annotation-demo")
public class AnnotationVsXmlServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Deployment Descriptor vs Annotations</title></head>");
        out.println("<body>");
        out.println("<h1>Deployment Descriptor vs Annotations</h1>");

        out.println("<h2>This Servlet Uses Annotations:</h2>");
        out.println("<p><code>@WebServlet(\"/annotation-demo\")</code></p>");
        out.println("<p>Short and useful for small applications.</p>");

        out.println("<h2>Annotation Approach:</h2>");
        out.println("<pre>");
        out.println("@WebServlet(\"/hello\")");
        out.println("public class HelloServlet extends HttpServlet {");
        out.println("    // servlet code");
        out.println("}");
        out.println("</pre>");

        out.println("<h2>Deployment Descriptor (web.xml) Approach:</h2>");
        out.println("<pre>");
        out.println("&lt;servlet&gt;");
        out.println("    &lt;servlet-name&gt;HelloServlet&lt;/servlet-name&gt;");
        out.println("    &lt;servlet-class&gt;HelloServlet&lt;/servlet-class&gt;");
        out.println("&lt;/servlet&gt;");
        out.println("");
        out.println("&lt;servlet-mapping&gt;");
        out.println("    &lt;servlet-name&gt;HelloServlet&lt;/servlet-name&gt;");
        out.println("    &lt;url-pattern&gt;/hello&lt;/url-pattern&gt;");
        out.println("&lt;/servlet-mapping&gt;");
        out.println("</pre>");

        out.println("<h2>Comparison:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><th>Feature</th><th>Annotation</th><th>web.xml</th></tr>");
        out.println("<tr><td>Configuration</td><td>In the Java class</td><td>In XML file</td></tr>");
        out.println("<tr><td>Best For</td><td>Small applications</td><td>Centralized configuration</td></tr>");
        out.println("<tr><td>Readability</td><td>Inline with code</td><td>Separate file</td></tr>");
        out.println("<tr><td>Changes</td><td>Requires recompilation</td><td>No recompilation needed</td></tr>");
        out.println("</table>");
        out.println("</body></html>");
    }
}
