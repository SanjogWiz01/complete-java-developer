final class SecurityManagerApp {
    final void authenticate(String user) {
        System.out.println("Authenticating user: " + user);
    }
}

class FinalClassesAndMethods {
    public static void main(String[] args) {
        SecurityManagerApp manager = new SecurityManagerApp();
        manager.authenticate("admin");

        System.out.println("SecurityManagerApp is final (cannot be extended).");
        System.out.println("authenticate() is final (cannot be overridden).");
    }
}
