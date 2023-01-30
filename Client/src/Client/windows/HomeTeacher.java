package Client.windows;

import javax.swing.*;
import java.awt.*;

public class HomeTeacher extends  JFrame{
    public JButton button1;
    public JButton button2;
    public JButton button3;
    private JPanel main;
    public JButton button4;
    public JButton logoutButton;
    private JPanel titre;
    public JLabel text;
    private JLabel classeName;

    public HomeTeacher(String name,String classe){
        text.setText("Teacher :"+ name);
        classeName.setText("classe: "+classe);
        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button4.setBorderPainted(false);
        button3.setBorderPainted(false);
        button1.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/chat.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button2.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/students.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button3.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/update_profil.jpg")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        button4.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/whiteboard.png")).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
        setContentPane(main);
        this.setVisible(true);
        this.setSize(700, 700);
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
