import java.awt.*;
import java.awt.event.*;

class AWT_CardLayout extends Frame {
    private CardLayout cardLayout;
    private Panel cardPanel;
    private int cardIndex = 0;

    public AWT_CardLayout() {
        setTitle("AWT CardLayout Demo");
        setSize(500, 300);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        cardPanel = new Panel();
        cardPanel.setLayout(cardLayout);

        Panel card1 = new Panel();
        card1.setBackground(new Color(200, 220, 240));
        card1.add(new Label("This is Card 1"));

        Panel card2 = new Panel();
        card2.setBackground(new Color(220, 200, 240));
        card2.add(new Label("This is Card 2"));

        Panel card3 = new Panel();
        card3.setBackground(new Color(240, 220, 200));
        card3.add(new Label("This is Card 3"));

        cardPanel.add("Card1", card1);
        cardPanel.add("Card2", card2);
        cardPanel.add("Card3", card3);

        Panel buttonPanel = new Panel();
        Button prevButton = new Button("Previous");
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(cardPanel);
            }
        });

        Button nextButton = new Button("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(cardPanel);
            }
        });

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        add(cardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AWT_CardLayout();
    }
}

