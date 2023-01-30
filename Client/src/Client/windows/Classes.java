package Client.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Classes extends JFrame{
    private JPanel main;
    public JTable table1;
    public JButton add;
    public JButton delete;
    public JButton update;
    public JButton refresh;
    public DefaultTableModel model;
    String[] header={"id","Name","teacher"};
    Object[][] liste;



    public Classes(){

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);






        setContentPane(main);
        this.setVisible(true);
        this.setSize(600, 500);
        this.setTitle("classes");
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
        setLocationRelativeTo(null);

    }
    public void getClasses(Object [][] liste){
        model=new DefaultTableModel();
        model.setColumnIdentifiers(header);
        for (int i=0;i<liste.length;i++)
            model.addRow(liste[i]);



        table1.setModel(model);

    }

    public static void main(String[] args) {
       Object[][] liste=new Object[1][3];
        liste[0][0]=1;
        liste[0][1]="mina";
        liste[0][2]="mina";


        Teachers teachers=new Teachers();
        teachers.getTecheras(liste);
    }
}
