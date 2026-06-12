import java.awt.*;
import java.awt.event.*;

public class awi_java {
    public static void main(String[] args) { 
        // Create a Frame 
        Frame frame = new Frame("AWT Example"); 
        Label label = new Label("Click the button!"); 
        label.setBounds(50, 50, 150, 20); // Set position and size 
        // Create a Button 
        Button button = new Button("Click Me"); 
        button.setBounds(200, 100, 80, 30); 
        // Add Action Listener for the Button 
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                label.setText("Button Clicked!"); 
            } 
        }); 
        // Add components to the Frame 
        frame.add(label); 
        frame.add(button); 
        // Set the size of the Frame 
        frame.setSize(400, 300); 
        // Set the visibility of the Frame 
        frame.setVisible(true); 
          // Close the frame when the user clicks the close button 
        frame.addWindowListener(new WindowAdapter() { 
            public void windowClosing(WindowEvent e) { 
                frame.dispose(); 
            } 
        }); 
    } 
} 
