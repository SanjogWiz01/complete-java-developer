import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ClientSessionManager {

    private static final String SESSION_USER_KEY = "sessionUser";
    private static final String SESSION_CART_KEY = "sessionCart";
    private static final String SESSION_CSRF_KEY = "csrfToken";

    private final HttpSession session;

    public ClientSessionManager(HttpServletRequest request) {
        this.session = request.getSession(true);
    }

    public void login(ClientUser user) {
        session.setAttribute(SESSION_USER_KEY, user);
        session.setMaxInactiveInterval(30 * 60);
    }

    public void logout() {
        session.invalidate();
    }

    public boolean isLoggedIn() {
        return session.getAttribute(SESSION_USER_KEY) != null;
    }

    public ClientUser getLoggedInUser() {
        return (ClientUser) session.getAttribute(SESSION_USER_KEY);
    }

    public void setCart(ClientShoppingCart cart) {
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public ClientShoppingCart getCart() {
        ClientShoppingCart cart = (ClientShoppingCart) session.getAttribute(SESSION_CART_KEY);
        if (cart == null) {
            cart = new ClientShoppingCart();
            session.setAttribute(SESSION_CART_KEY, cart);
        }
        return cart;
    }

    public void setCsrfToken(String token) {
        session.setAttribute(SESSION_CSRF_KEY, token);
    }

    public String getCsrfToken() {
        return (String) session.getAttribute(SESSION_CSRF_KEY);
    }

    public boolean isValidCsrf(String suppliedToken) {
        String storedToken = getCsrfToken();
        return storedToken != null && storedToken.equals(suppliedToken);
    }

    public void flashMessage(String type, String message) {
        session.setAttribute("flash_" + type, message);
    }

    public String consumeFlashMessage(String type) {
        String key = "flash_" + type;
        String message = (String) session.getAttribute(key);
        session.removeAttribute(key);
        return message;
    }
}
