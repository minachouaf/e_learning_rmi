package Client.windows;

import javax.swing.*;

public class Profil  extends JFrame{
    public JLabel name;
    public JLabel email;
    public JLabel classe;
    public JLabel note;
    private JPanel main;

    public  Profil(){
        setContentPane(main);
        this.setVisible(true);
        this.setSize(400, 400);
        this.setTitle("profil");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
    }
}
