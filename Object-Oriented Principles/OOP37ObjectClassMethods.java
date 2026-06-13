public class OOP37ObjectClassMethods {
    /*
     * Every Java class implicitly extends Object unless it extends another
     * class. Common Object methods include toString, equals, and hashCode.
     */
    static class Book {
        private final String isbn;
        private final String title;

        Book(String isbn, String title) {
            this.isbn = isbn;
            this.title = title;
        }

        @Override
        public String toString() {
            return "Book{isbn='" + isbn + "', title='" + title + "'}";
        }
    }

    public static void main(String[] args) {
        Book book = new Book("978-0134685991", "Effective Java");

        System.out.println(book.toString());
        System.out.println("Class name from Object: " + book.getClass().getSimpleName());
    }
}
