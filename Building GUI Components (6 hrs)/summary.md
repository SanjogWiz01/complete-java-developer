# Java GUI Development Compilation Guide  
## A Complete Beginner-to-Advanced Guide for AWT, Swing, and JavaFX

---

# Table of Contents

1. Introduction to GUI Programming
2. What is AWT?
3. What is Swing?
4. Applets
5. Swing Class Hierarchy
6. Components and Containers
7. Layout Management
8. GUI Controls
9. Menu Elements and Tooltips
10. Dialogs and Frames
11. Event Handling and Listener Interfaces
12. Handling Action Events
13. JavaFX vs Swing
14. JavaFX Layouts
15. JavaFX UI Controls
16. Mini Projects
17. Best Practices
18. Interview Questions
19. Learning Roadmap
20. Conclusion

---

# 1. Introduction to GUI Programming

GUI stands for **Graphical User Interface**.

Instead of interacting with applications using only text commands, GUI applications provide:

- Buttons
- Text fields
- Menus
- Windows
- Dialog boxes
- Checkboxes
- Images

Java provides multiple technologies for GUI development:

| Technology | Purpose |
|---|---|
| AWT | Basic GUI toolkit |
| Swing | Advanced GUI toolkit |
| JavaFX | Modern GUI framework |

---

# 2. What is AWT?

AWT stands for:

> Abstract Window Toolkit

It is Java’s first GUI framework.

Package:

```java
import java.awt.*;
```

## Features of AWT

- Platform dependent
- Uses native operating system components
- Heavyweight components
- Simple GUI development

## Example: Simple AWT Program

```java
import java.awt.*;

public class MyFrame {
    public static void main(String[] args) {

        Frame f = new Frame("My First AWT App");

        f.setSize(400, 300);
        f.setVisible(true);
    }
}
```

---

# 3. What is Swing?

Swing is an advanced GUI framework built on top of AWT.

Package:

```java
import javax.swing.*;
```

## Features of Swing

- Platform independent
- Lightweight components
- Rich UI controls
- Better customization
- MVC architecture support

## Example: Simple Swing Program

```java
import javax.swing.*;

public class MySwing {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Swing Example");

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}
```

---

# 4. Applets

An Applet is a Java program that runs inside a web browser.

## Important Notes

- Applets are outdated today
- Previously used for browser-based Java applications
- Requires Applet class

## Example

```java
import java.applet.Applet;
import java.awt.Graphics;

public class MyApplet extends Applet {

    public void paint(Graphics g) {
        g.drawString("Hello Applet", 100, 100);
    }
}
```

---

# 5. Swing Class Hierarchy

```text
Object
   |
Component
   |
Container
   |
JComponent
   |
-----------------------------------------
|        |        |        |            |
JButton JTextField JLabel JTable     JPanel
```

## Top-Level Containers

| Class | Purpose |
|---|---|
| JFrame | Main window |
| JDialog | Dialog window |
| JPanel | Group components |
| JApplet | Swing Applet |

---

# 6. Components and Containers

## Components

GUI elements visible on screen.

Examples:

- JButton
- JLabel
- JTextField
- JCheckBox
- JTable

## Containers

Hold components inside them.

Examples:

- JFrame
- JPanel
- JDialog

## Example

```java
import javax.swing.*;

public class Demo {
    public static void main(String[] args) {

        JFrame frame = new JFrame();

        JButton btn = new JButton("Click Me");

        frame.add(btn);

        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
```

---

# 7. Layout Management

Layout Managers control how components are arranged.

## Types of Layout Managers

| Layout | Purpose |
|---|---|
| FlowLayout | Left to right |
| BorderLayout | North, South, East, West, Center |
| GridLayout | Grid structure |
| CardLayout | Stack of cards |
| BoxLayout | Horizontal/Vertical arrangement |

---

## FlowLayout Example

```java
import javax.swing.*;
import java.awt.*;

public class FlowDemo {
    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setLayout(new FlowLayout());

        f.add(new JButton("One"));
        f.add(new JButton("Two"));

        f.setSize(300, 300);
        f.setVisible(true);
    }
}
```

---

## BorderLayout Example

```java
import javax.swing.*;
import java.awt.*;

public class BorderDemo {

    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setLayout(new BorderLayout());

        f.add(new JButton("North"), BorderLayout.NORTH);
        f.add(new JButton("South"), BorderLayout.SOUTH);

        f.setSize(400, 400);
        f.setVisible(true);
    }
}
```

---

# 8. GUI Controls

## JLabel

Displays text.

```java
JLabel label = new JLabel("Welcome");
```

---

## JButton

Creates buttons.

```java
JButton btn = new JButton("Submit");
```

---

## JTextField

Accepts single-line text input.

```java
JTextField tf = new JTextField(20);
```

---

## JTextArea

Multi-line text area.

```java
JTextArea area = new JTextArea();
```

---

## JCheckBox

Checkbox component.

```java
JCheckBox cb = new JCheckBox("Java");
```

---

## JRadioButton

Single selection option.

```java
JRadioButton rb = new JRadioButton("Male");
```

---

## JComboBox

Dropdown list.

```java
String[] items = {"C", "Java", "Python"};

JComboBox<String> box = new JComboBox<>(items);
```

---

## JTable

Displays data in tabular form.

```java
String data[][] = {
    {"1", "Ram"},
    {"2", "Hari"}
};

String col[] = {"ID", "Name"};

JTable table = new JTable(data, col);
```

---

# 9. Menu Elements and Tooltips

## JMenuBar

Main menu bar.

```java
JMenuBar mb = new JMenuBar();
```

---

## JMenu

Creates menu.

```java
JMenu file = new JMenu("File");
```

---

## JMenuItem

Menu option.

```java
JMenuItem open = new JMenuItem("Open");
```

---

## Tooltip

Small helper text.

```java
button.setToolTipText("Click this button");
```

---

## Complete Menu Example

```java
import javax.swing.*;

public class MenuDemo {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");

        file.add(open);

        mb.add(file);

        frame.setJMenuBar(mb);

        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
```

---

# 10. Dialogs and Frames

## JFrame

Main application window.

```java
JFrame frame = new JFrame();
```

---

## JDialog

Popup dialog window.

```java
JDialog dialog = new JDialog();
```

---

## JOptionPane

Simple dialog boxes.

### Message Dialog

```java
JOptionPane.showMessageDialog(null, "Hello");
```

### Input Dialog

```java
String name = JOptionPane.showInputDialog("Enter name");
```

### Confirm Dialog

```java
JOptionPane.showConfirmDialog(null, "Exit?");
```

---

# 11. Event Handling and Listener Interfaces

Events occur when users interact with GUI.

Examples:

- Button click
- Mouse click
- Keyboard press

## Event Delegation Model

### Three Parts

| Part | Description |
|---|---|
| Source | Component generating event |
| Event | Object representing event |
| Listener | Handles event |

---

## Common Listener Interfaces

| Listener | Purpose |
|---|---|
| ActionListener | Button click |
| MouseListener | Mouse events |
| KeyListener | Keyboard events |
| WindowListener | Window events |

---

# 12. Handling Action Events

## Example: Button Click Event

```java
import javax.swing.*;
import java.awt.event.*;

public class ActionDemo {

    public static void main(String[] args) {

        JFrame frame = new JFrame();

        JButton btn = new JButton("Click");

        btn.setBounds(100, 100, 100, 40);

        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(frame, "Button Clicked");
            }
        });

        frame.add(btn);

        frame.setSize(400, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
```

---

# 13. JavaFX vs Swing

| Feature | Swing | JavaFX |
|---|---|---|
| Appearance | Old | Modern |
| Graphics | Basic | Advanced |
| CSS Support | No | Yes |
| Multimedia | Limited | Strong |
| Performance | Good | Better |
| Styling | Hard | Easy |

---

# 14. JavaFX Layouts

JavaFX is the modern Java GUI framework.

Package:

```java
import javafx.application.Application;
```

---

## Important JavaFX Layouts

| Layout | Purpose |
|---|---|
| VBox | Vertical arrangement |
| HBox | Horizontal arrangement |
| BorderPane | Border structure |
| GridPane | Grid layout |
| StackPane | Stack elements |

---

## VBox Example

```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxDemo extends Application {

    public void start(Stage stage) {

        VBox root = new VBox();

        root.getChildren().add(new Button("Button 1"));
        root.getChildren().add(new Button("Button 2"));

        Scene scene = new Scene(root, 300, 300);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

---

# 15. JavaFX UI Controls

## Label

```java
Label label = new Label("Hello");
```

---

## Button

```java
Button btn = new Button("Submit");
```

---

## TextField

```java
TextField tf = new TextField();
```

---

## PasswordField

```java
PasswordField pf = new PasswordField();
```

---

## CheckBox

```java
CheckBox cb = new CheckBox("JavaFX");
```

---

## RadioButton

```java
RadioButton rb = new RadioButton("Male");
```

---

## ComboBox

```java
ComboBox<String> combo = new ComboBox<>();
```

---

## TableView

```java
TableView table = new TableView();
```

---

# 16. Mini Projects

## Beginner Projects

1. Calculator
2. Student Registration Form
3. Login Form
4. Notepad
5. To-Do App

---

## Intermediate Projects

1. Library Management System
2. Chat Application
3. Employee Management System
4. Banking System

---

## Advanced Projects

1. IDE Clone
2. Drawing Application
3. File Explorer
4. POS System

---

# 17. Best Practices

## Always Follow These

- Use meaningful variable names
- Separate GUI and logic
- Use layout managers properly
- Avoid hardcoding values
- Handle exceptions carefully
- Use JavaFX for modern applications

---

# 18. Interview Questions

## Basic Questions

### What is AWT?

AWT is Java’s original GUI toolkit using native platform components.

---

### Difference Between AWT and Swing?

| AWT | Swing |
|---|---|
| Heavyweight | Lightweight |
| Platform dependent | Platform independent |
| Less controls | More controls |

---

### What is Event Handling?

Handling user-generated actions like button clicks and keyboard events.

---

### What is Layout Manager?

Controls positioning of GUI components.

---

### Difference Between Swing and JavaFX?

JavaFX provides modern UI, CSS styling, multimedia, and advanced graphics.

---

# 19. Learning Roadmap

## Stage 1: Java Basics

Learn:

- Variables
- Loops
- OOP
- Arrays
- Methods

---

## Stage 2: AWT

Learn:

- Frames
- Buttons
- Labels
- Events

---

## Stage 3: Swing

Learn:

- Advanced components
- Menus
- Dialogs
- JTable
- Event handling

---

## Stage 4: JavaFX

Learn:

- Layouts
- Scene Builder
- CSS
- Animations

---

## Stage 5: Projects

Build:

- Calculator
- Login Form
- Management Systems

---

# 20. Conclusion

You now have a complete roadmap for learning:

- AWT
- Swing
- JavaFX

Mastering GUI development requires:

1. Strong Java basics
2. Daily practice
3. Building projects
4. Understanding event handling
5. Learning layouts deeply

The best way to improve is:

> Build projects consistently.

Good luck with your Java GUI journey.


 