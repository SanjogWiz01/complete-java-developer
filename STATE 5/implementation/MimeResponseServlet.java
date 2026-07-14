import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/mime")
public class MimeResponseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String format = request.getParameter("format");

        if (format == null) {
            format = "html";
        }

        switch (format) {
            case "json":
                serveJson(response);
                break;
            case "text":
                servePlainText(response);
                break;
            case "xml":
                serveXml(response);
                break;
            default:
                serveHtml(response, request);
                break;
        }
    }

    private void serveHtml(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>MIME Types</title></head>");
        out.println("<body>");
        out.println("<h1>MIME Request and Response Handling</h1>");

        out.println("<h2>Common MIME Content Types:</h2>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr><th>MIME Type</th><th>Usage</th></tr>");
        out.println("<tr><td>text/html</td><td>HTML pages</td></tr>");
        out.println("<tr><td>text/plain</td><td>Plain text</td></tr>");
        out.println("<tr><td>application/json</td><td>JSON APIs</td></tr>");
        out.println("<tr><td>application/xml</td><td>XML data</td></tr>");
        out.println("<tr><td>application/pdf</td><td>PDF files</td></tr>");
        out.println("</table>");

        out.println("<h2>Try Different Formats:</h2>");
        out.println("<ul>");
        out.println("<li><a href='mime?format=html'>HTML (default)</a></li>");
        out.println("<li><a href='mime?format=json'>JSON</a></li>");
        out.println("<li><a href='mime?format=text'>Plain Text</a></li>");
        out.println("<li><a href='mime?format=xml'>XML</a></li>");
        out.println("</ul>");

        out.println("<p>If the wrong content type is used, the browser may display or parse the response incorrectly.</p>");
        out.println("</body></html>");
    }

    private void serveJson(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().println("{\"status\":\"ok\",\"message\":\"This is a JSON response\",\"format\":\"application/json\"}");
    }

    private void servePlainText(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println("This is a plain text response.");
        response.getWriter().println("Content-Type: text/plain");
    }

    private void serveXml(HttpServletResponse response) throws IOException {
        response.setContentType("application/xml");
        response.getWriter().println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        response.getWriter().println("<response>");
        response.getWriter().println("  <status>ok</status>");
        response.getWriter().println("  <format>application/xml</format>");
        response.getWriter().println("</response>");
    }
}
