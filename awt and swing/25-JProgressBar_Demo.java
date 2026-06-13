import javax.swing.*;
import java.awt.*;

class JProgressBar_Demo extends JFrame {
    public JProgressBar_Demo() {
        setTitle("JProgressBar Demo");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JProgressBar progressBar1 = new JProgressBar(0, 100);
        progressBar1.setValue(50);
        progressBar1.setStringPainted(true);

        JProgressBar progressBar2 = new JProgressBar(0, 100);
        progressBar2.setValue(75);
        progressBar2.setStringPainted(true);
        progressBar2.setForeground(new Color(0, 150, 0));

        JProgressBar progressBar3 = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
        progressBar3.setValue(30);
        progressBar3.setStringPainted(true);

        JButton startButton = new JButton("Start Progress");
        startButton.addActionListener(e -> {
            Thread thread = new Thread(() -> {
                for (int i = 0; i <= 100; i += 5) {
                    int value = i;
                    SwingUtilities.invokeLater(() -> progressBar1.setValue(value));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.start();
        });

        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(new JLabel("Horizontal Progress:"), BorderLayout.NORTH);
        panel1.add(progressBar1, BorderLayout.CENTER);

        JPanel panel2 = new JPanel(new BorderLayout());
        panel2.add(new JLabel("Indeterminate Progress:"), BorderLayout.NORTH);
        panel2.add(progressBar2, BorderLayout.CENTER);

        add(panel1);
        add(panel2);
        add(startButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JProgressBar_Demo());
    }
}

