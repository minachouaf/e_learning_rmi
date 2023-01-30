package Client.Teacher;

import Client.Client;
import Client.Student.Student;
import Client.windows.*;
import Server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends UnicastRemoteObject implements Client {
    private Server server;
    public String userName;
    public String password;
    public String email;
    public Chat_Room_teacher chat_room_teacher;
    White_Board_teacher white_board_teacher;
    String classeName;
    public Teacher(Server server) throws RemoteException {
        this.server=server;
    }

    @Override
    public void sendfile(File file) throws RemoteException {
        try {
            ArrayList<Integer> data=new ArrayList<>();
            FileInputStream in=new FileInputStream(file);
            int readint=-1;
            while ((readint=in.read())!=-1){
                data.add(readint);
            }
            server.transfer_file(chat_room_teacher.connected_user.getSelectedValuesList(),file.getName(),data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive_message(String message) throws RemoteException {
        StyledDocument doc = chat_room_teacher.chat_room.getStyledDocument();
        System.out.println(message);
        try {
            doc.insertString(doc.getLength(), message + "\n", null);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void receive_file(List<Integer> data, String file) throws RemoteException {

        String path=System.getProperty("user.home")+"\\"+file;
        try {
            FileOutputStream out=new FileOutputStream(path);
            for (int i=0;i<data.size();i++){

                out.write((byte)((int)data.get(i)));

            }
            receive_message(file);
            JOptionPane.showMessageDialog(null, "you received a new file" +
                    "\n"+path);
            System.out.println("fin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(JFrame frame) throws RemoteException {
        Login loginn=new Login();

        Teacher teacher=this;
        loginn.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName=loginn.username.getText().trim();
                password=loginn.password.getText().trim();
                try {
                    String reponce = server.login(teacher,"teacher");
                    if (reponce.equals("connected")) {
                        System.out.println("connected");
                        frame.dispose();
                        loginn.dispose();
                        classeName=server.getClassbyTeachername(userName);
                        HomeTeacher homeTeacher=new HomeTeacher(userName,classeName);
                        homeTeacher.logoutButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                homeTeacher.dispose();
                                try {
                                    server.logout_teacher(userName);
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    Home home=new Home();
                                } catch (MalformedURLException ex) {
                                    ex.printStackTrace();
                                } catch (NotBoundException ex) {
                                    ex.printStackTrace();
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                         chat_room_teacher=new Chat_Room_teacher();
                        homeTeacher.button4.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                white_board_teacher=new White_Board_teacher();
                                try {
                                    white_board_teacher.connected_user.setListData(server.getConnected_teacher_student(userName).toArray());
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                                white_board_teacher.refreshButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            white_board_teacher.connected_user.setListData(server.getConnected_teacher_student(userName).toArray());
                                        } catch (RemoteException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                                white_board_teacher.saveButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        int heigth=white_board_teacher.board.getHeight();
                                        int width=white_board_teacher.board.getWidth();
                                        int x=white_board_teacher.board.getX();
                                        int y=white_board_teacher.board.getY();
                                        try {
                                            Robot robot=new Robot();
                                            Rectangle rectangle =white_board_teacher.board.getBounds();
                                            String path=System.getProperty("user.home")+"\\"+"board"+userName+".jpg";
                                            BufferedImage img=robot.createScreenCapture(rectangle);

                                            ImageIO.write(img,"JPG", new File(path));



                                        } catch (AWTException ex) {
                                            ex.printStackTrace();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                });
                                white_board_teacher.clearButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {

                                        clear();
                                        try {
                                            send_shape("clear",new ArrayList<>());
                                        } catch (RemoteException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });

                                white_board_teacher.board.addMouseListener(new MouseListener() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {

                                    }

                                    @Override
                                    public void mousePressed(MouseEvent e) {
                                        if(white_board_teacher.Action.equals("write")) {
                                            white_board_teacher.list_points.add(e.getPoint());

                                        }
                                        else {
                                            white_board_teacher.pressed = e.getPoint();
                                        }

                                    }

                                    @Override
                                    public void mouseReleased(MouseEvent e) {
                                        if(white_board_teacher.Action.equals("write")){
                                            try {
                                                send_shape("write",white_board_teacher.list_points);
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        if(white_board_teacher.Action.equals("erase")){
                                            try {
                                                send_shape("erase",white_board_teacher.list_points);
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        ArrayList<Point> pressed_released=new ArrayList<>();
                                        pressed_released.add(white_board_teacher.pressed);
                                        pressed_released.add(e.getPoint());
                                        if(white_board_teacher.Action.equals("rectangle")){
                                            draw_Rectangle(white_board_teacher.pressed,e.getPoint());
                                            try {
                                                send_shape("rectangle",pressed_released);
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        if(white_board_teacher.Action.equals("oval")){
                                            draw_Oval(white_board_teacher.pressed,e.getPoint());
                                            try {
                                                send_shape("oval",pressed_released);
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        if(white_board_teacher.Action.equals("line")){
                                            draw_Line(white_board_teacher.pressed,e.getPoint());
                                            try {
                                                send_shape("line",pressed_released);
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        white_board_teacher.list_points.clear();
                                    }

                                    @Override
                                    public void mouseEntered(MouseEvent e) {

                                    }

                                    @Override
                                    public void mouseExited(MouseEvent e) {

                                    }
                                });
                                white_board_teacher.board.addMouseMotionListener(new MouseMotionListener() {
                                    @Override
                                    public void mouseDragged(MouseEvent e) {
                                        if(white_board_teacher.Action.equals("write")){
                                            white_board_teacher.list_points.add(e.getPoint());
                                            write();



                                        }
                                        if(white_board_teacher.Action.equals("erase")){
                                            white_board_teacher.list_points.add(e.getPoint());
                                            eraser();

                                        }

                                    }

                                    @Override
                                    public void mouseMoved(MouseEvent e) {

                                    }
                                });
                            }

                    });


                                                 homeTeacher.button3.addActionListener(new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {



                                                                            try {
                                                                                ArrayList<String> liste=server.get_teacher_byname(userName);

                                                                                Update_teacher update_teacher = new Update_teacher();
                                                                                update_teacher.username.setText(liste.get(0));
                                                                                update_teacher.email.setText(liste.get(1));
                                                                                update_teacher.password.setText(liste.get(2));

                                                                                update_teacher.updateButton.addActionListener(new ActionListener() {
                                                                                    @Override
                                                                                    public void actionPerformed(ActionEvent e) {
                                                                                        String userName = update_teacher.username.getText().trim();
                                                                                        String password = update_teacher.password.getText().trim();
                                                                                        String email = update_teacher.email.getText().trim();
                                                                                        String confirmed = update_teacher.confirmed.getText().trim();

                                                                                        if (confirmed.equals(password)) {

                                                                                            String reponce = null;
                                                                                            try {
                                                                                                reponce = server.updateTeacher(liste.get(3), userName, email, password);
                                                                                            } catch (RemoteException ex) {
                                                                                                ex.printStackTrace();
                                                                                            }
                                                                                            if (reponce.equals("ok")) {
                                                                                                setUserName(userName);
                                                                                                setPassword(password);
                                                                                                homeTeacher.text.setText("Teacher :"+userName);

                                                                                                System.out.println("ok");
                                                                                                update_teacher.error.setText("teacher updated successfully");
                                                                                                update_teacher.username.setText("");
                                                                                                update_teacher.password.setText("");
                                                                                                update_teacher.email.setText("");
                                                                                                update_teacher.confirmed.setText("");
                                                                                            } else {
                                                                                                update_teacher.error.setText("teacher alreday exists");
                                                                                            }
                                                                                        } else {
                                                                                            update_teacher.error.setText("no valid confirmed password");
                                                                                        }
                                                                                    }
                                                                                });


                                                                            } catch (RemoteException ex) {
                                                                                ex.printStackTrace();
                                                                            }
                                                                        }





                                                            });

                        homeTeacher.button2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                Object[][] liste = server.getStudents_byteacher(userName);
                                Students_note students_note = new Students_note();
                                students_note.getStudents(liste);

                                students_note.refresh.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            students_note.getStudents(server.getStudents_byteacher(userName));
                                        } catch (RemoteException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                                    students_note.update.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String row = "";
                                            if (students_note.table1.getSelectedRow()!=-1) {
                                                row = students_note.model.getValueAt(students_note.table1.getSelectedRow(), 0).toString();
                                                System.out.println(row);

                                                try {
                                                    ArrayList<String> liste=server.getStudent(row);
                                                    Update_note update_note=new Update_note();
                                                    update_note.username.setText(liste.get(0));
                                                    update_note.email.setText(liste.get(1));
                                                    update_note.note.setText(liste.get(3));
                                                    update_note.username.setEditable(false);
                                                    update_note.email.setEditable(false);

                                                    String finalRow = row;
                                                    update_note.updateButton.addActionListener(new ActionListener() {
                                                        @Override
                                                        public void actionPerformed(ActionEvent e) {
                                                            String note=update_note.note.getText().trim();

                                                            if(Double.parseDouble(note)>=0 || Double.parseDouble(note)<=20){
                                                                String reponce = null;
                                                                try {
                                                                    reponce = server.updateStudent(finalRow,note);
                                                                } catch (RemoteException ex) {
                                                                    ex.printStackTrace();
                                                                }
                                                                if (reponce.equals("ok")) {

                                                                    update_note.error.setText("student updated successfully");




                                                                }} else {
                                                                    update_note.error.setText("no valide note");
                                                                }

                                                        }
                                                    });


                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }}

                                    });
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }

                            }
                                    });
                        homeTeacher.button1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                              chat_room_teacher.setVisible(true);

                                chat_room_teacher.setTitle("teacher : " + userName);


                                try {
                                    chat_room_teacher.connected_user.setListData(server.getConnected_teacher_student(userName).toArray());
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                                chat_room_teacher.refreshButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {


                                        try {
                                            chat_room_teacher.connected_user.setListData(server.getConnected_teacher_student(userName).toArray());
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                });

                                chat_room_teacher.sendButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {
                                        List<String> lite=chat_room_teacher.connected_user.getSelectedValuesList();
                                        try {
                                            server.transfer_message(lite,userName+" : "+chat_room_teacher.message_field.getText());
                                            chat_room_teacher.message_field.setText("");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                chat_room_teacher.browsButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {


                                        JFileChooser fc = new JFileChooser();

                                        int res = fc.showOpenDialog(null);

                                        try {
                                            if (res == JFileChooser.APPROVE_OPTION) {
                                                File file = fc.getSelectedFile();
                                                sendfile(file);



                                            }
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }} });
                            }
                        });


                    }
                    else {
                        loginn.error.setText("incorect data");
                    }
                }catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });


    }
    public void register(JFrame frame)throws RemoteException {
        Register register=new Register();
        Teacher student=this;

        register.registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName=register.username.getText().trim();
                password=register.password.getText().trim();
                email=register.email.getText().trim();
                String confirmed=register.confirmed.getText().trim();
                try {
                    if(confirmed.equals(password)){
                        String reponce = server.register(student,"student");
                        if (reponce.equals("ok")) {
                            System.out.println("ok");
                            register.dispose();
                            login(frame);

                        }
                        else{
                            register.error.setText("incorrect data");
                        }
                    }
                    else{
                        register.error.setText("non valid confirmed password");
                    }


                }catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void clear(){
        White_Board_teacher.graph.setPaint(Color.white);

        White_Board_teacher.graph.fillRect(0, 0, white_board_teacher.board.getSize().width, white_board_teacher.board.getSize().height);
        White_Board_teacher.graph.setPaint(Color.black);
        white_board_teacher.list_points.clear();
        white_board_teacher.board.repaint();

    }
    public void eraser(){
        White_Board_teacher.graph.setPaint(Color.white);
        int taille=white_board_teacher.list_points.size();

        White_Board_teacher.graph.clearRect(white_board_teacher.list_points.get(taille-1).x,white_board_teacher.list_points.get(taille-1).y,10,10);
        White_Board_teacher.graph.setPaint(Color.black);
    }

    public void draw_Oval(Point p1,Point p2){


        int w = Math.abs(p2.x - p1.x);
        int h = Math.abs(p2.y - p1.y);
        int x=Math.min(p1.x,p2.x);
        int y=Math.min(p1.y,p2.y);
        White_Board_teacher.graph.drawOval(x,y, w, h);
    }
    public void draw_Line(Point p1,Point p2){


        White_Board_teacher.graph.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void write(){

        int taille=white_board_teacher.list_points.size();
        White_Board_teacher.graph.drawLine(white_board_teacher.list_points.get(taille-2).x, white_board_teacher.list_points.get(taille-2).y,white_board_teacher.list_points.get(taille-1).x,white_board_teacher.list_points.get(taille-1).y);
    }
    public void draw_Rectangle(Point p1,Point p2){


        int w = Math.abs(p2.x - p1.x);
        int h = Math.abs(p2.y - p1.y);
        int x=Math.min(p1.x,p2.x);
        int y=Math.min(p1.y,p2.y);
        White_Board_teacher.graph.drawRect(x,y, w, h);
    }

    @Override
    public String getUsername() throws RemoteException{
        return userName;
    }

    @Override
    public String getPassword() throws RemoteException{
        return password;
    }
    @Override
    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void send_shape(String operation, ArrayList<Point> points) throws RemoteException {
        server.transfer_shape(operation,points,userName);
    }

    @Override
    public void receive_shape(String operation, ArrayList<Point> points) throws RemoteException {
        System.out.println(operation);

        if(operation.equals("write")){
            for (int i=0;i<points.size()-1;i++)
                White_Board_teacher.graph.drawLine(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);

        }

        if(operation.equals("erase")){

            for (int i=0;i<points.size()-1;i++)
                White_Board_teacher.graph.clearRect(points.get(i).x, points.get(i).y, 20, 20);

        }
        if(operation.equals("clear"))
        {
            clear();
        }


        if(operation.equals("line"))
        {
            Point p1=points.get(0);
            Point p2=points.get(1);
            draw_Line(p1,p2);

        }
        if(operation.equals("rectangle"))
        {
            Point p1=points.get(0);
            Point p2=points.get(1);

            draw_Rectangle(p1,p2);
        }
        if(operation.equals("oval"))
        {
            Point p1=points.get(0);
            Point p2=points.get(1);

            draw_Oval(p1,p2);
        }


    }



}

