package Client.windows;

import javax.swing.*;

public class Update_teacher extends JFrame{
    private JPanel main;
    public JButton updateButton;
    public JTextField email;
    public JPasswordField password;
    public JPasswordField confirmed;
    public JTextField username;
    public JLabel error;

    public Update_teacher(){

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
