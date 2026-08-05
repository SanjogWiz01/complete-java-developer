import jakarta.servlet.http.HttpServletRequest;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClientRequest {

    private String action;
    private final Map<String, String> fields;

    public ClientRequest() {
        this.fields = new LinkedHashMap<>();
    }

    public static ClientRequest from(HttpServletRequest request) {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.action = trim(request.getParameter("action"));
        for (String paramName : request.getParameterMap().keySet()) {
            clientRequest.fields.put(paramName, trim(request.getParameter(paramName)));
        }
        return clientRequest;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getField(String name) {
        return fields.get(name);
    }

    public void putField(String name, String value) {
        fields.put(name, value);
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public boolean hasField(String name) {
        String value = fields.get(name);
        return value != null && !value.trim().isEmpty();
    }

    public String getMethod() {
        return "POST".equalsIgnoreCase(action) ? "POST" : "GET";
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
