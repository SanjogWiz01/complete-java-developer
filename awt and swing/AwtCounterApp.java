import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AwtCounterApp extends Frame {
    private int count = 0;
    private final TextField countField;

    public AwtCounterApp() {
        super("AWT Counter App");

        Label title = new Label("Simple AWT Counter", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        countField = new TextField("0", 10);
        countField.setEditable(false);
        countField.setFont(new Font("Arial", Font.PLAIN, 16));

        Button increaseButton = new Button("Increase");
        Button resetButton = new Button("Reset");

        increaseButton.addActionListener(event -> {
            count++;
            countField.setText(String.valueOf(count));
        });

        resetButton.addActionListener(event -> {
            count = 0;
            countField.setText(String.valueOf(count));
        });

        Panel controls = new Panel(new FlowLayout());
        controls.add(new Label("Count:"));
        controls.add(countField);
        controls.add(increaseButton);
        controls.add(resetButton);

        add(title, BorderLayout.NORTH);
        add(controls, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                dispose();
            }
        });

        setSize(360, 160);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new AwtCounterApp().setVisible(true);
    }
}
