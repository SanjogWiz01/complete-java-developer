package unit7.patterns;

interface Button {
    void render();
}

interface TextField {
    void render();
}

interface UIFactory {
    Button createButton();
    TextField createTextField();
}

class DesktopButton implements Button {
    public void render() { System.out.println("Desktop button"); }
}

class DesktopTextField implements TextField {
    public void render() { System.out.println("Desktop text field"); }
}

class MobileButton implements Button {
    public void render() { System.out.println("Mobile button"); }
}

class MobileTextField implements TextField {
    public void render() { System.out.println("Mobile text field"); }
}

class DesktopUIFactory implements UIFactory {
    public Button createButton() { return new DesktopButton(); }
    public TextField createTextField() { return new DesktopTextField(); }
}

class MobileUIFactory implements UIFactory {
    public Button createButton() { return new MobileButton(); }
    public TextField createTextField() { return new MobileTextField(); }
}

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        UIFactory factory = new MobileUIFactory();

        Button button = factory.createButton();
        TextField textField = factory.createTextField();

        button.render();
        textField.render();
    }
}
