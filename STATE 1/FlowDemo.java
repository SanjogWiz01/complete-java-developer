import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

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
//  commentr added 
