public class OOP15MultilevelInheritance {
    /*
     * Multilevel inheritance creates a chain: grandparent -> parent -> child.
     */
    static class LivingThing {
        void grow() {
            System.out.println("Living thing grows.");
        }
    }

    static class Plant extends LivingThing {
        void photosynthesize() {
            System.out.println("Plant makes food from sunlight.");
        }
    }

    static class Flower extends Plant {
        void bloom() {
            System.out.println("Flower blooms.");
        }
    }

    public static void main(String[] args) {
        Flower flower = new Flower();
        flower.grow();
        flower.photosynthesize();
        flower.bloom();
    }
}
