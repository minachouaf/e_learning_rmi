package Client.windows;

import javax.swing.*;
import java.util.ArrayList;

public class Add_classe extends JFrame{
    private JPanel main;
    public JButton addButton;
    public JTextField username;
    public JLabel error;
    public JComboBox comboBox1;
    DefaultComboBoxModel model=new DefaultComboBoxModel<>();

    public Add_classe(ArrayList<String> liste){

for(int i=0;i<liste.size();i++)
{
    model.addAll(liste);
}
        comboBox1.setModel(model);
        setContentPane(main);
        this.setVisible(true);
        this.setSize(400, 400);
        this.setTitle("Add classe");
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());

    }

    public static void main(String[] args) {
        ArrayList<String> liste=new ArrayList<>();
        liste.add("mina");
        liste.add("sara");
        Add_classe add_classe=new Add_classe(liste);
    }


}
