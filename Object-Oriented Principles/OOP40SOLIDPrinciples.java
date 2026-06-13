public class OOP40SOLIDPrinciples {
    /*
     * SOLID is a practical checklist for maintainable object-oriented design:
     * S: Single Responsibility Principle.
     * O: Open/Closed Principle.
     * L: Liskov Substitution Principle.
     * I: Interface Segregation Principle.
     * D: Dependency Inversion Principle.
     */
    interface AreaShape {
        double area();
    }

    interface Drawable {
        void draw();
    }

    static class Circle implements AreaShape, Drawable {
        private final double radius;

        Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double area() {
            return Math.PI * radius * radius;
        }

        @Override
        public void draw() {
            System.out.println("Drawing circle.");
        }
    }

    static class AreaCalculator {
        double totalArea(AreaShape[] shapes) {
            double total = 0;
            for (AreaShape shape : shapes) {
                total += shape.area();
            }
            return total;
        }
    }

    public static void main(String[] args) {
        AreaShape[] shapes = {new Circle(3)};
        AreaCalculator calculator = new AreaCalculator();

        System.out.println("Total area: " + calculator.totalArea(shapes));
    }
}
