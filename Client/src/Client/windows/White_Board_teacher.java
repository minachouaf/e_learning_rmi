package Client.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class White_Board_teacher extends JFrame {
    private JPanel main;
    public JList connected_user;
    public JPanel board;
    private JButton rectangleButton;
    public JButton clearButton;

    public JButton saveButton;
    private JButton writeButton;
    private JButton lineButton;
    private JButton ovalButton;
    private JButton eraserButton;
    public JButton refreshButton;
    public Point pressed;
    public static Graphics2D graph;
    public ArrayList<Point> list_points;
    public String Action="write";

    public White_Board_teacher(){

list_points=new ArrayList<>();

       setContentPane(main);
        setSize(1200,600);
       setVisible(true);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graph = (Graphics2D) board.getGraphics();
        graph.setStroke(new BasicStroke(3));
        setTitle("board");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
        rectangleButton.setBorderPainted(false);
        ovalButton.setBorderPainted(false);
        clearButton.setBorderPainted(false);
        saveButton.setBorderPainted(false);
        lineButton.setBorderPainted(false);
        writeButton.setBorderPainted(false);

        eraserButton.setBorderPainted(false);
        rectangleButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/rectangle.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        ovalButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/oval.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        clearButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/clear.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        saveButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/save.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        lineButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/line.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        writeButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/write.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));

        eraserButton.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/eraser.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));
        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action="line";
            }
        });
        ovalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action="oval";
            }
        });

        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action="write";
            }
        });
        rectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action="rectangle";
            }
        });
        eraserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action="erase";
            }
        });

    }

    public static void main(String[] args) {
        White_Board_teacher white_board=new White_Board_teacher();
    }
}
