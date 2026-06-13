public class OOP22Upcasting {
    /*
     * Upcasting treats a child object as its parent type. It is safe and happens
     * automatically because every child is also a parent.
     */
    static class Animal {
        void speak() {
            System.out.println("Animal sound.");
        }
    }

    static class Dog extends Animal {
        @Override
        void speak() {
            System.out.println("Dog barks.");
        }

        void fetch() {
            System.out.println("Dog fetches.");
        }
    }

    public static void main(String[] args) {
        Animal animal = new Dog();
        animal.speak();

        Dog dog = new Dog();
        Animal upcastDog = dog;
        upcastDog.speak();
    }
}
