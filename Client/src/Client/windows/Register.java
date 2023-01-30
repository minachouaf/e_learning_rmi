package Client.windows;

import javax.swing.*;

public class Register extends JFrame{
    private JPanel main;
    public JButton registerButton;
    public JTextField email;
    public JPasswordField password;
    public JPasswordField confirmed;
    public JTextField username;
    public JLabel error;

    public Register(){

        setContentPane(main);
        this.setVisible(true);
        this.setSize(470, 400);
        this.setTitle("Register");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());

    }

    public static void main(String[] args) {
        Add_teacher registerAdmin=new Add_teacher();
    }


}
