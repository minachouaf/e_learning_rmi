package Client.windows;

import javax.swing.*;

public class Chat_Room extends JFrame {
    public JTextField message_field;
    public JList connected_user;
    public JButton sendButton;
    public JButton refreshButton;
    public JButton browsButton;
    private JPanel pan;
    private JPanel panel1;
    public JTextPane chat_room;

    public Chat_Room(){


        chat_room.setEditable(false);

        setSize(600, 500);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setVisible(false);
        this.setTitle("CHAT ROOM");

        setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());
    }

    public static void main(String[] args) {
        Chat_Room chat_room=new Chat_Room();
    }
}
