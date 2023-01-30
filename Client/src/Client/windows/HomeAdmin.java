package Client.windows;

import javax.swing.*;
import java.awt.*;

public class HomeAdmin extends  JFrame{
    public JButton button1;
    public JButton button2;
    public JButton button3;
    private JPanel main;
    public JButton button4;
    public JButton logoutButton;
    private JPanel n;
    private JLabel text;

    public HomeAdmin(String name){
        text.setText("Admin :"+ name);
        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button4.setBorderPainted(false);
        button3.setBorderPainted(false);
        button1.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/teachers.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button2.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/students.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button3.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/inscrire.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button4.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/classe.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        setContentPane(main);
        this.setVisible(true);
        this.setSize(700, 700);
        this.setTitle("Home page");
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        HomeAdmin homeAdmin=new HomeAdmin("mina");
    }
}
