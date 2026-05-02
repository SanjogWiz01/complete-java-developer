interface Chargeable {
    void charge();
}

abstract class Device {
    protected final String model;

    Device(String model) {
        this.model = model;
    }

    abstract void start();

    void stop() {
        System.out.println(model + " is shutting down.");
    }
}

class Laptop extends Device implements Chargeable {
    Laptop(String model) {
        super(model);
    }

    @Override
    void start() {
        System.out.println(model + " boots into the operating system.");
    }

    @Override
    public void charge() {
        System.out.println(model + " is charging via USB-C.");
    }
}

public class AbstractClassesAndInterfaces {
    public static void main(String[] args) {
        Laptop laptop = new Laptop("ThinkPro X");
        laptop.start();
        laptop.charge();
        laptop.stop();
    }
}
