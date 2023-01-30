package Client.windows;

import javax.swing.*;

public class Login extends JFrame{
    private JPanel main;
    public JButton loginButton;
    public JTextField username;
    public JTextField password;
    public JLabel error;

    public Login(){
        setContentPane(main);
        this.setVisible(true);
        this.setSize(400, 350);
        this.setTitle("Login");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
    }

    public static void main(String[] args) {
        Login login =new Login();
    }
}
