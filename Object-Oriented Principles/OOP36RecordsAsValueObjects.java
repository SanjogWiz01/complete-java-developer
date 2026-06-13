public class OOP36RecordsAsValueObjects {
    /*
     * Records are concise immutable data carriers. Java automatically provides
     * accessors, equals, hashCode, and toString.
     */
    record Point(int x, int y) {
        Point {
            if (x < 0 || y < 0) {
                throw new IllegalArgumentException("Coordinates must be positive.");
            }
        }

        int distanceFromOriginSquared() {
            return x * x + y * y;
        }
    }

    public static void main(String[] args) {
        Point point = new Point(3, 4);
        System.out.println(point);
        System.out.println("Distance squared: " + point.distanceFromOriginSquared());
    }
}
