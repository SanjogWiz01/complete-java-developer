public class OOP05ConstructorOverloadingAndChaining {
    /*
     * Constructor overloading provides multiple ways to create an object.
     * Chaining with this(...) avoids duplicated initialization logic.
     */
    static class Rectangle {
        private final int width;
        private final int height;

        Rectangle() {
            this(1, 1);
        }

        Rectangle(int side) {
            this(side, side);
        }

        Rectangle(int width, int height) {
            this.width = width;
            this.height = height;
        }

        int area() {
            return width * height;
        }
    }

    public static void main(String[] args) {
        System.out.println("Default rectangle area: " + new Rectangle().area());
        System.out.println("Square area: " + new Rectangle(5).area());
        System.out.println("Custom rectangle area: " + new Rectangle(4, 6).area());
    }
}
