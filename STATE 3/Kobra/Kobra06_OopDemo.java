public class Kobra06_OopDemo {

    public static void main(String[] args) {
        Animal dog = new Dog("Rex");
        Animal cat = new Cat("Whiskers");

        dog.speak();
        cat.speak();
        System.out.println(dog);
    }
}

class Animal {
    protected String name;

    Animal(String name) {
        this.name = name;
    }

    void speak() {
        System.out.println(name + " makes a sound.");
    }

    @Override
    public String toString() {
        return "Animal{name='" + name + "'}";
    }
}

class Dog extends Animal {
    Dog(String name) {
        super(name);
    }

    @Override
    void speak() {
        System.out.println(name + " barks: Woof woof!");
    }
}

class Cat extends Animal {
    Cat(String name) {
        super(name);
    }

    @Override
    void speak() {
        System.out.println(name + " meows: Meow!");
    }
}
