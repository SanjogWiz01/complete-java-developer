import javax.swing.*;

class Threading_SwingWorker extends JFrame {
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public Threading_SwingWorker() {
        setTitle("SwingWorker Threading Demo");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        statusLabel = new JLabel("Status: Ready");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JButton startButton = new JButton("Start Long Task");
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            startLongTask();
        });

        add(statusLabel);
        add(progressBar);
        add(startButton);

        setVisible(true);
    }

    private void startLongTask() {
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 0; i <= 100; i += 10) {
                    publish(i);
                    Thread.sleep(500);
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int latest = chunks.get(chunks.size() - 1);
                progressBar.setValue(latest);
                statusLabel.setText("Status: Processing... " + latest + "%");
            }

            @Override
            protected void done() {
                statusLabel.setText("Status: Completed!");
                progressBar.setValue(100);
            }
        };
        
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Threading_SwingWorker());
    }
}

