public class OOP34InnerLocalAndAnonymousClasses {
    /*
     * Inner classes need an outer object. Local classes live inside a method.
     * Anonymous classes create a one-time implementation without a class name.
     */
    interface Formatter {
        String format(String value);
    }

    class PrefixFormatter implements Formatter {
        @Override
        public String format(String value) {
            return "Outer prefix: " + value;
        }
    }

    void demonstrate() {
        class LocalFormatter implements Formatter {
            @Override
            public String format(String value) {
                return "Local prefix: " + value;
            }
        }

        Formatter anonymousFormatter = new Formatter() {
            @Override
            public String format(String value) {
                return "Anonymous prefix: " + value;
            }
        };

        System.out.println(new PrefixFormatter().format("Java"));
        System.out.println(new LocalFormatter().format("OOP"));
        System.out.println(anonymousFormatter.format("Class"));
    }

    public static void main(String[] args) {
        new OOP34InnerLocalAndAnonymousClasses().demonstrate();
    }
}
