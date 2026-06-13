public class OOP13InheritanceBasics {
    /*
     * Inheritance lets a child class reuse and specialize behavior from a parent
     * class. Use extends for class inheritance.
     */
    static class User {
        private final String username;

        User(String username) {
            this.username = username;
        }

        String loginMessage() {
            return username + " logged in.";
        }
    }

    static class AdminUser extends User {
        AdminUser(String username) {
            super(username);
        }

        String openAdminPanel() {
            return "Admin panel opened.";
        }
    }

    public static void main(String[] args) {
        AdminUser admin = new AdminUser("root");
        System.out.println(admin.loginMessage());
        System.out.println(admin.openAdminPanel());
    }
}
