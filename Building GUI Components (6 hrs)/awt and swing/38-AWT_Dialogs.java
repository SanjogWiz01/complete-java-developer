import java.awt.*;
import java.awt.event.*;

class AWT_Dialogs extends Frame {
    public AWT_Dialogs() {
        setTitle("AWT Dialog Demo");
        setSize(500, 300);
        setLayout(new FlowLayout());

        Button dialogButton = new Button("Show Dialog");
        dialogButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dialog dialog = new Dialog(AWT_Dialogs.this, "Sample Dialog", true);
                dialog.setSize(300, 150);
                dialog.setLayout(new FlowLayout());
                dialog.add(new Label("This is a modal dialog"));
                
                Button closeButton = new Button("Close");
                closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ce) {
                        dialog.setVisible(false);
                    }
                });
                dialog.add(closeButton);
                dialog.setVisible(true);
            }
        });

        add(dialogButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AWT_Dialogs();
    }
}

