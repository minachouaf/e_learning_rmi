package Client.Student;



import Client.Client;
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

public class Student extends UnicastRemoteObject implements Client {
    private Server server;
    public String userName;
    public String password;
    public String email;
    public Chat_Room room;
    White_Board white_board;
    String classename;
    public Student(Server server) throws RemoteException {
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
            server.transfer_file(room.connected_user.getSelectedValuesList(),file.getName(),data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive_message(String message) throws RemoteException {
        StyledDocument doc = room.chat_room.getStyledDocument();
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

        Student student=this;
        loginn.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName=loginn.username.getText().trim();
                password=loginn.password.getText().trim();
                try {
                    String reponce = server.login(student,"student");
                    if (reponce.equals("connected")) {
                        System.out.println("connected");
                         classename=server.getClassbyStudentname(userName);
                         frame.dispose();
                        loginn.dispose();


                        String nameclass=server.getClassbyStudentname(userName);
                            HomeStudent homeStudent = new HomeStudent(userName,nameclass);
                        ArrayList<String> classesName = new ArrayList<>();
                        if(nameclass.equals("")) {
                            classesName = server.getClassesName();
                        }
                        Inscription_student inscription_student=new Inscription_student(classesName);
                        homeStudent.logoutButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                homeStudent.dispose();
                                try {
                                    server.logout_student(userName);
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
                        homeStudent.button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                      inscription_student.setVisible(true);
                                    inscription_student.addButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                String reponce=  server.addInscription(userName, (String) inscription_student.comboBox1.getSelectedItem());
                                                if(classename!=null) {
                                                    if (reponce.equals("ok")) {
                                                        System.out.println("ok");
                                                        inscription_student.error.setText("inscription added successfully");

                                                        inscription_student.comboBox1.setSelectedItem("");
                                                        homeStudent.setClasseName(server.getClassbyStudentname(userName));
                                                        inscription_student.vidercomboBox();


                                                    } else {
                                                        inscription_student.error.setText("already exist");
                                                    }
                                                }
                                                else{
                                                    inscription_student.error.setText("already enrolled");
                                                }
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

                                }
                            });
                        room = new Chat_Room();

                        homeStudent.button1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                room.setVisible(true);
                                room.setTitle("student: " + userName);


                                try {
                                    ArrayList<String> students= (ArrayList<String>) server.getConnectedstudent(userName);
                                    String name_teacher=server.getClasse_teacher(classename);
                                    if (name_teacher != "")
                                    {
                                        students.add("T_" + name_teacher);

                                    }
                                    room.connected_user.setListData(students.toArray());
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                                room.sendButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {
                                        List<String> lite=room.connected_user.getSelectedValuesList();
                                        try {
                                            server.transfer_message(lite,userName+" : "+room.message_field.getText());
                                            room.message_field.setText("");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                room.refreshButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent actionEvent) {


                                        try {
                                            ArrayList<String> students = (ArrayList<String>) server.getConnectedstudent(userName);
                                            String name_teacher = server.getClasse_teacher(classename);
                                            if (name_teacher != "")
                                            {
                                                students.add("T_" + name_teacher);

                                        }
                                            room.connected_user.setListData(students.toArray());
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                });

                                room.browsButton.addActionListener(new ActionListener() {
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

                        homeStudent.profilButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                              Profil profil=new Profil();

                                ArrayList<String> liste= null;
                                try {
                                    liste = server.get_student_byname(userName);
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }


                                profil.name.setText(liste.get(0));
                                profil.email.setText(liste.get(1));
                                profil.classe.setText(classename);
                                profil.note.setText(liste.get(4));
                            }
                        });
                        homeStudent.button3.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {



                                try {
                                    ArrayList<String> liste=server.get_student_byname(userName);

                                    Update_student update_student = new Update_student();
                                    update_student.username.setText(liste.get(0));
                                    update_student.email.setText(liste.get(1));
                                    update_student.password.setText(liste.get(2));

                                    update_student.updateButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            String userName = update_student.username.getText().trim();
                                            String password = update_student.password.getText().trim();
                                            String email = update_student.email.getText().trim();
                                            String confirmed = update_student.confirmed.getText().trim();

                                            if (confirmed.equals(password)) {

                                                String reponce = null;
                                                try {
                                                    reponce = server.updateStudent(liste.get(3), userName, email, password);
                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }
                                                if (reponce.equals("ok")) {
                                                    setUserName(userName);
                                                    setPassword(password);
                                                    homeStudent.text.setText("Student :"+userName);

                                                    System.out.println("ok");
                                                    update_student.error.setText("profil updated successfully");
                                                    update_student.username.setText("");
                                                    update_student.password.setText("");
                                                    update_student.email.setText("");
                                                    update_student.confirmed.setText("");
                                                } else {
                                                    update_student.error.setText("profil alreday exists");
                                                }
                                            } else {
                                                update_student.error.setText("no valid confirmed password");
                                            }
                                        }
                                    });


                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }





                        });
                        homeStudent.button4.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                white_board=new White_Board();
                                try {
                                    get_user();
                                    white_board.refreshButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            try {
                                                white_board.connected_user.setText("");
                                                get_user();

                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            } catch (BadLocationException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                                    white_board.saveButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            int heigth=white_board.board.getHeight();
                                            int width=white_board.board.getWidth();
                                            int x=white_board.board.getX();
                                            int y=white_board.board.getY();
                                            try {
                                                Robot robot=new Robot();
                                                Rectangle rectangle =white_board.board.getBounds();
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
                                    white_board.clearButton.addActionListener(new ActionListener() {
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

                                  white_board.board.addMouseListener(new MouseListener() {
                                      @Override
                                      public void mouseClicked(MouseEvent e) {

                                      }

                                      @Override
                                      public void mousePressed(MouseEvent e) {
                                          if(white_board.Action.equals("write")) {
                                              white_board.list_points.add(e.getPoint());

                                          }
                                          else {
                                              white_board.pressed = e.getPoint();
                                          }

                                      }

                                      @Override
                                      public void mouseReleased(MouseEvent e) {
                                          if(white_board.Action.equals("write")){
                                              try {
                                                  send_shape("write",white_board.list_points);
                                              } catch (RemoteException ex) {
                                                  ex.printStackTrace();
                                              }
                                          }
                                          if(white_board.Action.equals("erase")){
                                              try {
                                                  send_shape("erase",white_board.list_points);
                                              } catch (RemoteException ex) {
                                                  ex.printStackTrace();
                                              }
                                          }
                                          ArrayList<Point> pressed_released=new ArrayList<>();
                                          pressed_released.add(white_board.pressed);
                                          pressed_released.add(e.getPoint());
                                          if(white_board.Action.equals("rectangle")){
                                              draw_Rectangle(white_board.pressed,e.getPoint());
                                              try {
                                                  send_shape("rectangle",pressed_released);
                                              } catch (RemoteException ex) {
                                                  ex.printStackTrace();
                                              }
                                          }
                                          if(white_board.Action.equals("oval")){
                                              draw_Oval(white_board.pressed,e.getPoint());
                                              try {
                                                  send_shape("oval",pressed_released);
                                              } catch (RemoteException ex) {
                                                  ex.printStackTrace();
                                              }
                                          }
                                          if(white_board.Action.equals("line")){
                                              draw_Line(white_board.pressed,e.getPoint());
                                              try {
                                                  send_shape("line",pressed_released);
                                              } catch (RemoteException ex) {
                                                  ex.printStackTrace();
                                              }
                                          }
                                          white_board.list_points.clear();
                                      }

                                      @Override
                                      public void mouseEntered(MouseEvent e) {

                                      }

                                      @Override
                                      public void mouseExited(MouseEvent e) {

                                      }
                                  });
                                  white_board.board.addMouseMotionListener(new MouseMotionListener() {
                                      @Override
                                      public void mouseDragged(MouseEvent e) {
                                          if(white_board.Action.equals("write")){
                                              white_board.list_points.add(e.getPoint());
                                              write();



                                          }
                                          if(white_board.Action.equals("erase")){
                                              white_board.list_points.add(e.getPoint());
                                              eraser();

                                          }

                                      }

                                      @Override
                                      public void mouseMoved(MouseEvent e) {

                                      }
                                  });
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
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
        Student student=this;

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
    @Override
    public String getUsername() throws RemoteException{
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() throws RemoteException{
        return password;
    }
    @Override
    public String getEmail() {
        return email;
    }
    public void get_user() throws RemoteException, BadLocationException {
        ArrayList<String> students= (ArrayList<String>) server.getConnectedstudent(userName);
        String name_teacher=server.getClasse_teacher(classename);
        StyledDocument doc = white_board.connected_user.getStyledDocument();

        doc.insertString(doc.getLength(), name_teacher + "\n", null);
        for(int i=0;i<students.size();i++){
            doc.insertString(doc.getLength(), students.get(i) + "\n", null);
        }
    }
    @Override
    public void send_shape(String operation, ArrayList<Point> points) throws RemoteException {
        server.transfer_shape_student(operation,points,userName);
    }
    public void clear(){
        White_Board.graph.setPaint(Color.white);

        White_Board.graph.fillRect(0, 0, white_board.board.getSize().width, white_board.board.getSize().height);
        White_Board.graph.setPaint(Color.black);
        white_board.list_points.clear();
        white_board.board.repaint();

    }
    public void eraser(){
        White_Board.graph.setPaint(Color.white);
        int taille=white_board.list_points.size();

        White_Board.graph.clearRect(white_board.list_points.get(taille-1).x,white_board.list_points.get(taille-1).y,10,10);
        White_Board.graph.setPaint(Color.black);
    }

    public void draw_Oval(Point p1,Point p2){


        int w = Math.abs(p2.x - p1.x);
        int h = Math.abs(p2.y - p1.y);
        int x=Math.min(p1.x,p2.x);
        int y=Math.min(p1.y,p2.y);
        White_Board.graph.drawOval(x,y, w, h);
    }
    public void draw_Line(Point p1,Point p2){


        White_Board.graph.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void write(){

        int taille=white_board.list_points.size();
        White_Board.graph.drawLine(white_board.list_points.get(taille-2).x, white_board.list_points.get(taille-2).y,white_board.list_points.get(taille-1).x,white_board.list_points.get(taille-1).y);
    }
    public void draw_Rectangle(Point p1,Point p2){


        int w = Math.abs(p2.x - p1.x);
        int h = Math.abs(p2.y - p1.y);
        int x=Math.min(p1.x,p2.x);
        int y=Math.min(p1.y,p2.y);
        White_Board.graph.drawRect(x,y, w, h);
    }
    @Override
    public void receive_shape(String operation, ArrayList<Point> points) throws RemoteException {
        System.out.println(operation);

        if(operation.equals("write")){
            for (int i=0;i<points.size()-1;i++)
                White_Board.graph.drawLine(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y);

        }

        if(operation.equals("erase")){

            for (int i=0;i<points.size()-1;i++)
                White_Board.graph.clearRect(points.get(i).x, points.get(i).y, 20, 20);

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
