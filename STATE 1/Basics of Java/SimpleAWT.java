import java.awt.*;
import java.awt.event.*;

public class SimpleAWT {
    public static void main(String[] args) {
        Frame frame = new Frame("AWT Example");
        Label label = new Label("Click the button!");
        label.setBounds(50, 50, 150, 20);
        label.setVisible(false);

        Button btn = new Button("Click Me");
        btn.setBounds(200, 100, 80, 30);
        btn.setFocusable(false);
        btn.addActionListener(new ActionListener() {
            boolean toggle = false;
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                if (count > 5) {
                    System.out.println("STFU");
                } else {
                    System.out.println("Btn Clicked: " + count);
                }
                if (!toggle) {
                    label.setText("Button Clicked");
                    label.setVisible(true);
                } else {
                    label.setVisible(false);
                }
                toggle = !toggle;
            }

        });
        frame.add(label);
        frame.add(btn);
        frame.setSize(400, 350);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });

    }

}
