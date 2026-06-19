public class OOP08AccessModifiers {
    /*
     * Java access levels:
     * public: accessible everywhere.
     * protected: package access plus subclasses.
     * package-private: no keyword, accessible inside the same package.
     * private: accessible only inside the declaring class.
     */
    static class AccessDemo {
        public String publicInfo = "public";
        protected String protectedInfo = "protected";
        String packageInfo = "package-private";
        private String privateInfo = "private";

        String revealPrivateInfo() {
            return privateInfo;
        }
    }

    public static void main(String[] args) {
        AccessDemo demo = new AccessDemo();

        System.out.println(demo.publicInfo);
        System.out.println(demo.protectedInfo);
        System.out.println(demo.packageInfo);
        System.out.println(demo.revealPrivateInfo());
    }
}
