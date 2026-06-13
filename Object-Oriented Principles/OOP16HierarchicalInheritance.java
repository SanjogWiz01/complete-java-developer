public class OOP16HierarchicalInheritance {
    /*
     * Hierarchical inheritance means multiple child classes extend the same
     * parent class.
     */
    static class Shape {
        void draw() {
            System.out.println("Drawing a generic shape.");
        }
    }

    static class Circle extends Shape {
        void radiusInfo() {
            System.out.println("Circle has one radius.");
        }
    }

    static class Triangle extends Shape {
        void sideInfo() {
            System.out.println("Triangle has three sides.");
        }
    }

    public static void main(String[] args) {
        Circle circle = new Circle();
        Triangle triangle = new Triangle();

        circle.draw();
        circle.radiusInfo();
        triangle.draw();
        triangle.sideInfo();
    }
}
