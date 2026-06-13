import javax.swing.*;
import java.awt.*;

class MusicPlayer_Project extends JFrame {
    private JLabel songLabel;
    private JSlider progressSlider;
    private boolean isPlaying = false;

    public MusicPlayer_Project() {
        setTitle("Music Player");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel displayPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        songLabel = new JLabel("No song selected");
        songLabel.setFont(new Font("Arial", Font.BOLD, 18));
        songLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel timeLabel = new JLabel("00:00 / 03:45");
        timeLabel.setHorizontalAlignment(JLabel.CENTER);

        progressSlider = new JSlider(0, 100, 0);
        progressSlider.setPaintTicks(true);

        displayPanel.add(songLabel);
        displayPanel.add(progressSlider);
        displayPanel.add(timeLabel);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton previousButton = new JButton("⏮ Previous");
        previousButton.addActionListener(e -> songLabel.setText("Playing Previous Song"));

        JButton playButton = new JButton("▶ Play");
        playButton.addActionListener(e -> {
            isPlaying = !isPlaying;
            playButton.setText(isPlaying ? "⏸ Pause" : "▶ Play");
        });

        JButton nextButton = new JButton("⏭ Next");
        nextButton.addActionListener(e -> songLabel.setText("Playing Next Song"));

        JButton stopButton = new JButton("⏹ Stop");
        stopButton.addActionListener(e -> {
            isPlaying = false;
            playButton.setText("▶ Play");
            songLabel.setText("Stopped");
        });

        controlPanel.add(previousButton);
        controlPanel.add(playButton);
        controlPanel.add(nextButton);
        controlPanel.add(stopButton);

        JPanel playlistPanel = new JPanel(new BorderLayout());
        playlistPanel.setBorder(BorderFactory.createTitledBorder("Playlist"));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Song 1 - Artist 1");
        listModel.addElement("Song 2 - Artist 2");
        listModel.addElement("Song 3 - Artist 3");

        JList<String> playlistList = new JList<>(listModel);
        playlistList.addListSelectionListener(e -> 
            songLabel.setText("Now Playing: " + playlistList.getSelectedValue())
        );

        playlistPanel.add(new JScrollPane(playlistList), BorderLayout.CENTER);

        add(displayPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(playlistPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicPlayer_Project());
    }
}

