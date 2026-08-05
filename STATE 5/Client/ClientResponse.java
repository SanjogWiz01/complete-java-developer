import java.util.LinkedHashMap;
import java.util.Map;

public class ClientResponse {

    private boolean success;
    private String message;
    private String redirectUrl;
    private final Map<String, Object> data;

    public ClientResponse() {
        this.data = new LinkedHashMap<>();
    }

    public ClientResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }

    public static ClientResponse ok(String message) {
        return new ClientResponse(true, message);
    }

    public static ClientResponse error(String message) {
        return new ClientResponse(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean hasErrors() {
        return !success;
    }
}
