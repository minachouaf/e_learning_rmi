package Client.windows;

import javax.swing.*;
import java.awt.*;

public class HomeStudent extends  JFrame{
    public JButton button1;
    public JButton button2;
    public JButton button3;
    private JPanel main;
    public JButton button4;
    public JButton logoutButton;
    private JPanel n;
    public JLabel text;
    private JLabel classeName;
    public JButton profilButton;

    public HomeStudent(String name,String classe){
        text.setText("Student :"+ name);
        classeName.setText("classe: "+classe);
        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button4.setBorderPainted(false);
        button3.setBorderPainted(false);
        profilButton.setBorderPainted(false);
        profilButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/profil2.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        button1.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/chat.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button3.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/update_profil.jpg")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button2.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/inscrire.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button4.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/whiteboard.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        setContentPane(main);
        this.setVisible(true);
        this.setSize(800, 700);
        this.setTitle("Home page");
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
        setLocationRelativeTo(null);
    }

    public void setClasseName(String name) {
        classeName.setText("classe: "+name);
    }

    public static void main(String[] args) {
        HomeStudent homeStudent=new HomeStudent("mina","irisi");
    }
}
