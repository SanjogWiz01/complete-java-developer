import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ClientCookieManager {

    private static final int ONE_YEAR_SECONDS = 365 * 24 * 60 * 60;

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public ClientCookieManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void set(String name, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void setRememberMe(String value) {
        set("rememberMe", value, ONE_YEAR_SECONDS);
    }

    public void setTheme(String theme) {
        set("theme", theme, ONE_YEAR_SECONDS);
    }

    public void setLastVisit(long timestamp) {
        set("lastVisit", String.valueOf(timestamp), ONE_YEAR_SECONDS);
    }

    public String get(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public String getThemeOrDefault(String fallback) {
        String theme = get("theme");
        return theme == null || theme.isEmpty() ? fallback : theme;
    }

    public boolean has(String name) {
        return get(name) != null;
    }

    public void remove(String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
