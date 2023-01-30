package Client.windows;

import javax.swing.*;
import java.util.ArrayList;

public class Add_inscription extends JFrame{
    private JPanel main;
    public JButton addButton;
    public JLabel error;
    public JComboBox comboBox1;
    public JComboBox comboBox2;
    DefaultComboBoxModel model=new DefaultComboBoxModel<>();
    DefaultComboBoxModel model2=new DefaultComboBoxModel<>();

    public Add_inscription (ArrayList<String> liste,ArrayList<String> students){

for(int i=0;i<liste.size();i++)
{
    model.addAll(liste);
}

            model2.addAll(students);


        comboBox1.setModel(model);
        comboBox2.setModel(model2);
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
