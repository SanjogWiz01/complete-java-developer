import java.awt.*;

class AWT_BasicComponents extends Frame {
    private Label label;
    private Button button;
    private TextField textField;
    private TextArea textArea;
    private Checkbox checkbox;

    public AWT_BasicComponents() {
        setTitle("AWT Basic Components");
        setSize(500, 400);
        setLayout(new FlowLayout());

        label = new Label("Hello AWT!");
        button = new Button("Click Me");
        textField = new TextField("Enter text here", 20);
        textArea = new TextArea(5, 30);
        checkbox = new Checkbox("Accept Terms");

        add(label);
        add(button);
        add(textField);
        add(textArea);
        add(checkbox);

        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        new AWT_BasicComponents();
    }
}

