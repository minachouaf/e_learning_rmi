package Client.windows;

import javax.swing.*;

public class Update_note extends JFrame{
    private JPanel main;
    public JButton updateButton;
    public JTextField email;
    public JTextField note;
    public JTextField username;
    public JLabel error;
    private JLabel n;

    public Update_note(){

        setContentPane(main);
        this.setVisible(true);
        this.setSize(470, 400);
        this.setTitle("Add teacher");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());

    }

    public static void main(String[] args) {
        Add_teacher registerAdmin=new Add_teacher();
    }


}
