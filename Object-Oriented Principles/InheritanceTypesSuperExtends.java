import java.util.ArrayList;
import java.util.List;

class InheritanceTypesSuperExtends {
    static class Animal {
        protected String name;

        Animal(String name) {
            this.name = name;
        }

        void speak() {
            System.out.println(name + " makes a sound.");
        }
    }

    static class Dog extends Animal {
        Dog(String name) {
            super(name); // use of super keyword
        }

        @Override
        void speak() {
            System.out.println(name + " barks.");
        }
    }

    static class GuideDog extends Dog {
        GuideDog(String name) {
            super(name);
        }

        void guide() {
            System.out.println(name + " is guiding safely.");
        }
    }

    static void printAnimals(List<? extends Animal> animals) { // use of extends wildcard
        for (Animal animal : animals) {
            animal.speak();
        }
    }

    public static void main(String[] args) {
        Animal a = new Animal("GenericAnimal");
        Dog d = new Dog("Buddy");
        GuideDog g = new GuideDog("Hero");

        a.speak();
        d.speak();
        g.speak();
        g.guide();

        List<Dog> dogs = new ArrayList<>();
        dogs.add(d);
        dogs.add(g);
        printAnimals(dogs);
    }
}
