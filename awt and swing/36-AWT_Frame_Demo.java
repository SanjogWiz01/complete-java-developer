import java.awt.*;
import java.awt.event.*;

class AWT_Frame_Demo extends Frame {
    private Label statusLabel;
    private TextField textInput;

    public AWT_Frame_Demo() {
        setTitle("AWT Frame Demo");
        setSize(500, 300);
        setLocation(100, 100);
        setLayout(new FlowLayout());

        Label titleLabel = new Label("Welcome to AWT!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        statusLabel = new Label("Status: Ready");
        
        textInput = new TextField("Enter text here", 20);
        
        Button submitButton = new Button("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Submitted: " + textInput.getText());
            }
        });

        Button clearButton = new Button("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textInput.setText("");
                statusLabel.setText("Status: Cleared");
            }
        });

        add(titleLabel);
        add(textInput);
        add(submitButton);
        add(clearButton);
        add(statusLabel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AWT_Frame_Demo();
    }
}

