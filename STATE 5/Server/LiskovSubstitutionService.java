package server;

import java.util.List;
import java.util.ArrayList;

public class LiskovSubstitutionService {

    public abstract static class Bird {
        public abstract void eat();
    }

    public abstract static class FlyingBird extends Bird {
        public abstract void fly();
    }

    public abstract static class NonFlyingBird extends Bird {
    }

    public static class Sparrow extends FlyingBird {
        @Override
        public void eat() {
            System.out.println("Sparrow is eating seeds.");
        }

        @Override
        public void fly() {
            System.out.println("Sparrow is flying high.");
        }
    }

    public static class Penguin extends NonFlyingBird {
        @Override
        public void eat() {
            System.out.println("Penguin is eating fish.");
        }

        public void swim() {
            System.out.println("Penguin is swimming.");
        }
    }

    public static class BirdSanctuary {
        private final List<Bird> birds = new ArrayList<>();

        public void addBird(Bird bird) {
            birds.add(bird);
        }

        public void feedAllBirds() {
            for (Bird bird : birds) {
                bird.eat();
                if (bird instanceof FlyingBird) {
                    ((FlyingBird) bird).fly();
                }
                if (bird instanceof Penguin) {
                    ((Penguin) bird).swim();
                }
            }
        }
    }

    public static void main(String[] args) {
        BirdSanctuary sanctuary = new BirdSanctuary();
        sanctuary.addBird(new Sparrow());
        sanctuary.addBird(new Penguin());
        sanctuary.feedAllBirds();
    }
}
