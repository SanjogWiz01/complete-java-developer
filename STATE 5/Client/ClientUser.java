import java.io.Serializable;

public class ClientUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String role;
    private String createdAt;

    public ClientUser() {
        this.role = "USER";
    }

    public ClientUser(int id, String username, String email, String passwordHash,
                      String fullName, String role, String createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public String getDisplayName() {
        return (fullName == null || fullName.trim().isEmpty()) ? username : fullName;
    }
}
