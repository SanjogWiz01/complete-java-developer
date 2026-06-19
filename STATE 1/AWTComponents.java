import java.awt.*; 
class AWTComponents { 
    AWTComponents() { 
        Frame f = new Frame("AWT Components Example"); 
        // Components 
        Label l1 = new Label("Name:"); 
        Label l2 = new Label("Gender:"); 
        Label l3 = new Label("Course:"); 
        TextField tf = new TextField(20); 
        Checkbox c1 = new Checkbox("Male"); 
        Checkbox c2 = new Checkbox("Female"); 
        Choice ch = new Choice(); 
        ch.add("BCA"); 
        ch.add("BIT"); 
        ch.add("BSc CSIT"); 
        Button b1 = new Button("Submit"); 
        Button b2 = new Button("Reset"); 
        TextArea ta = new TextArea(4, 30); 
        // Layout 
        f.setLayout(new FlowLayout()); 
        // Add components 
        f.add(l1); 
        f.add(tf); 
        f.add(l2); 
        f.add(c1); 
        f.add(c2); 
        f.add(l3); 
        f.add(ch); 
        f.add(b1); 
        f.add(b2); 
        f.add(ta); 
        // Frame settings 
        f.setSize(400, 350); 
        f.setVisible(true); 
    } 
    public static void main(String[] args) { 
        new AWTComponents();
    }
}