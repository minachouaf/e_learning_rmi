package Client.Admin;




import Client.Client;
import Client.windows.*;
import Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Admin extends UnicastRemoteObject implements Client {

    private Server server;
    public String userName;
    public String password;
    public String email;






    public Admin(Server server) throws RemoteException {
        super();
        this.server=server;

    }

    public void login(JFrame frame) throws RemoteException {
        Login loginn=new Login();

        Admin admin=this;
        loginn.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName=loginn.username.getText().trim();
                password=loginn.password.getText().trim();
                try {
                    String reponce = server.login(admin,"admin");
                    if (reponce.equals("connected")) {
                        System.out.println("connected");
                        frame.dispose();
                        loginn.dispose();

                        HomeAdmin homeAdmin=new HomeAdmin(userName);
                        homeAdmin.button1.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Object[][] liste=server.getTeachers();
                                    Teachers teacher=new Teachers();
                                    teacher.getTecheras(liste);
                                    teacher.delete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("test");
                                            if (teacher.table1.getSelectedRow()!=-1) {
                                                String row = teacher.model.getValueAt(teacher.table1.getSelectedRow(), 0).toString();
                                                System.out.println(row);
                                                int t= Integer.parseInt(row);
                                                System.out.println(t);
                                                try {
                                                    String responce=server.deleteTeacher(row);
                                                    if(responce.equals("ok"))
                                                    JOptionPane.showMessageDialog(null, "teacher deleted successfully");
                                                    else{
                                                        JOptionPane.showMessageDialog(null, "teacher cannot be deleted");
                                                    }
                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        }

                                    });
                                    teacher.refresh.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            try {
                                                teacher.getTecheras(server.getTeachers());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }

                                        }
                                    });
                                    teacher.add.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                        Add_teacher add_teacher=new Add_teacher();
                                        add_teacher.addButton.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {

                                                String userName=add_teacher.username.getText().trim();
                                                String password=add_teacher.password.getText().trim();
                                                String email=add_teacher.email.getText().trim();
                                                String confirmed=add_teacher.confirmed.getText().trim();
                                                try {
                                                    if(confirmed.equals(password)){
                                                        String reponce = server.addTeacher(userName,email,password);
                                                        if (reponce.equals("ok")) {
                                                            System.out.println("ok");
                                                            add_teacher.error.setText("teacher added successfully");
                                                           add_teacher.username.setText("");
                                                            add_teacher.password.setText("");
                                                            add_teacher.email.setText("");
                                                            add_teacher.confirmed.setText("");

                                                        }
                                                        else{
                                                            add_teacher.error.setText("already exist");
                                                        }
                                                    }
                                                    else{
                                                        add_teacher.error.setText("no valid confirmed password");
                                                    }


                                                }catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }
                                            }
                                        });
                                        teacher.update.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                String row = "";
                                                if (teacher.table1.getSelectedRow()!=-1) {
                                                     row = teacher.model.getValueAt(teacher.table1.getSelectedRow(), 0).toString();
                                                    System.out.println(row);

                                                    try {
                                                        ArrayList<String> liste=server.getteacher(row);
                                                        Update_teacher update_teacher=new Update_teacher();
                                                        update_teacher.username.setText(liste.get(0));
                                                        update_teacher.email.setText(liste.get(1));
                                                        update_teacher.password.setText(liste.get(2));
                                                        String finalRow = row;
                                                        update_teacher.updateButton.addActionListener(new ActionListener() {
                                                            @Override
                                                            public void actionPerformed(ActionEvent e) {
                                                                String userName=update_teacher.username.getText().trim();
                                                                String password=update_teacher.password.getText().trim();
                                                                String email=update_teacher.email.getText().trim();
                                                                String confirmed=update_teacher.confirmed.getText().trim();

                                                                if(confirmed.equals(password)) {

                                                                    String reponce = null;
                                                                    try {
                                                                        reponce = server.updateTeacher(finalRow, userName, email, password);
                                                                    } catch (RemoteException ex) {
                                                                        ex.printStackTrace();
                                                                    }
                                                                    if (reponce.equals("ok")) {
                                                                        System.out.println("ok");
                                                                        update_teacher.error.setText("teacher updated successfully");
                                                                        update_teacher.username.setText("");
                                                                        update_teacher.password.setText("");
                                                                        update_teacher.email.setText("");
                                                                        update_teacher.confirmed.setText("");
                                                                    } else {
                                                                        update_teacher.error.setText("teacher alreday exists");
                                                                    }
                                                                }
                                                                else{
                                                                    update_teacher.error.setText("no valid confirmed password");
                                                                }
                                                            }
                                                        });


                                                    } catch (RemoteException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }}

                                        });
                                        }
                                    });
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });



                        homeAdmin.button2.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Object[][] liste=server.getStudents();
                                    Students students=new Students();
                                    students.getStudents(liste);
                                    students.delete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("test");
                                            if (students.table1.getSelectedRow()!=-1) {
                                                String row = students.model.getValueAt(students.table1.getSelectedRow(), 0).toString();
                                                System.out.println(row);
                                                int t= Integer.parseInt(row);
                                                System.out.println(t);
                                                try {
                                                   String reponce= server.deleteStudent(row);
                                                   if(reponce.equals("ok"))
                                                    JOptionPane.showMessageDialog(null, "student deleted successfully");
                                                   else
                                                   {
                                                       JOptionPane.showMessageDialog(null, "student cannot be deleted ");
                                                   }
                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        }

                                    });
                                    students.refresh.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            try {
                                                students.getStudents(server.getStudents());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }

                                        }
                                    });
                                    students.add.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            Add_student add_student=new Add_student();
                                            add_student.addButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {

                                                    String userName=add_student.username.getText().trim();
                                                    String password=add_student.password.getText().trim();
                                                    String email=add_student.email.getText().trim();
                                                    String confirmed=add_student.confirmed.getText().trim();
                                                    try {
                                                        if(confirmed.equals(password)){
                                                            String reponce = server.addStudent(userName,email,password);
                                                            if (reponce.equals("ok")) {
                                                                System.out.println("ok");
                                                                add_student.error.setText("student added successfully");
                                                                add_student.username.setText("");
                                                                add_student.password.setText("");
                                                                add_student.email.setText("");
                                                                add_student.confirmed.setText("");

                                                            }
                                                            else{
                                                                add_student.error.setText("already exists");
                                                            }
                                                        }
                                                        else{
                                                            add_student.error.setText("no valid confirmed password");
                                                        }


                                                    }catch (RemoteException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            });
                                            students.update.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String row = "";
                                                    if (students.table1.getSelectedRow()!=-1) {
                                                        row = students.model.getValueAt(students.table1.getSelectedRow(), 0).toString();
                                                        System.out.println(row);

                                                        try {
                                                            ArrayList<String> liste=server.getStudent(row);
                                                            Update_student update_student=new Update_student();
                                                            update_student.username.setText(liste.get(0));
                                                            update_student.email.setText(liste.get(1));
                                                            update_student.password.setText(liste.get(2));
                                                            String finalRow = row;
                                                            update_student.updateButton.addActionListener(new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {
                                                                    String userName=update_student.username.getText().trim();
                                                                    String password=update_student.password.getText().trim();
                                                                    String email=update_student.email.getText().trim();
                                                                    String confirmed=update_student.confirmed.getText().trim();

                                                                    if(confirmed.equals(password)) {

                                                                        String reponce = null;
                                                                        try {
                                                                            reponce = server.updateStudent(finalRow, userName, email, password);
                                                                        } catch (RemoteException ex) {
                                                                            ex.printStackTrace();
                                                                        }
                                                                        if (reponce.equals("ok")) {
                                                                            System.out.println("ok");
                                                                            update_student.error.setText("student updated successfully");
                                                                            update_student.username.setText("");
                                                                            update_student.password.setText("");
                                                                            update_student.email.setText("");
                                                                            update_student.confirmed.setText("");
                                                                        } else {
                                                                            update_student.error.setText("studentt already exist");
                                                                        }
                                                                    }
                                                                    else{
                                                                        update_student.error.setText("no valid confirmed password");
                                                                    }
                                                                }
                                                            });


                                                        } catch (RemoteException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }}

                                            });
                                        }
                                    });
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        homeAdmin.button3.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Object[][] liste=server.getInscriptions();
                                    Inscription inscription=new Inscription();
                                    inscription.getInscription(liste);
                                    inscription.delete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("test");
                                            if (inscription.table1.getSelectedRow()!=-1) {
                                                String row = inscription.model.getValueAt(inscription.table1.getSelectedRow(), 0).toString();
                                                System.out.println(row);
                                                int t= Integer.parseInt(row);
                                                System.out.println(t);
                                                try {
                                                    String reponce=server.deleteInscription(row);
                                                    if(reponce.equals("ok"))
                                                    JOptionPane.showMessageDialog(null, "Inscription deleted successfully");
                                                    else
                                                        JOptionPane.showMessageDialog(null, "operation failled");
                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        }

                                    });
                                    inscription.refresh.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            try {
                                                inscription.getInscription(server.getInscriptions());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }

                                        }
                                    });
                                    inscription.add.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            Add_inscription add_inscription= null;
                                            try {
                                                add_inscription = new Add_inscription(server.getClassesName(),server.getStudentsName());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }

                                            Add_inscription finalAdd_inscription = add_inscription;
                                            Add_inscription finalAdd_inscription1 = add_inscription;
                                            add_inscription.addButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {

                                                    String student= (String) finalAdd_inscription.comboBox2.getSelectedItem();
                                                    String classe= (String) finalAdd_inscription.comboBox1.getSelectedItem();

                                                    try {

                                                        String reponce = server.addInscription(student,classe);
                                                        if (reponce.equals("ok")) {
                                                            System.out.println("ok");
                                                            finalAdd_inscription1.error.setText("inscription added successfully");
                                                            finalAdd_inscription1.comboBox2.setSelectedItem("");
                                                            finalAdd_inscription1.comboBox1.setSelectedItem("");


                                                        }
                                                        else{
                                                            finalAdd_inscription1.error.setText("already exist");
                                                        }
                                                    }



                                                    catch (RemoteException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            });
                                            inscription.update.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String row = "";
                                                    if (inscription.table1.getSelectedRow()!=-1) {
                                                        row = inscription.model.getValueAt(inscription.table1.getSelectedRow(), 0).toString();
                                                        System.out.println(row);

                                                        try {
                                                            ArrayList<String> liste=server.getInscription(row);
                                                            ArrayList<String>  student_names=server.getStudentsName();
                                                            ArrayList<String>  classes_names=server.getClassesName();
                                                            Update_inscription update_inscription=new Update_inscription(classes_names,student_names);
                                                            update_inscription.comboBox2.setSelectedItem(liste.get(0));
                                                            update_inscription.comboBox1.setSelectedItem(liste.get(1));

                                                            String finalRow = row;
                                                            update_inscription.updateButton.addActionListener(new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {
                                                                    String studentName= (String) update_inscription.comboBox2.getSelectedItem();
                                                                    String classe_name= (String) update_inscription.comboBox1.getSelectedItem();




                                                                    String reponce = null;
                                                                    try {
                                                                        reponce = server.updateInscription(finalRow, studentName,classe_name);
                                                                    } catch (RemoteException ex) {
                                                                        ex.printStackTrace();
                                                                    }
                                                                    if (reponce.equals("ok")) {
                                                                        System.out.println("ok");
                                                                        update_inscription.error.setText("inscription updated successfully");
                                                                        update_inscription.comboBox2.setSelectedItem("");
                                                                        update_inscription.comboBox1.setSelectedItem("");

                                                                    } else {
                                                                        update_inscription.error.setText("Student already affected");
                                                                    }


                                                                }
                                                            });


                                                        } catch (RemoteException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }}

                                            });
                                        }
                                    });
                                } catch (RemoteException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        homeAdmin.logoutButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {


                                    homeAdmin.dispose();
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

                        homeAdmin.button4.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    Object[][] liste=server.getClasses();
                                    Classes classes=new Classes();
                                    classes.getClasses(liste);
                                    classes.delete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            System.out.println("test");
                                            if (classes.table1.getSelectedRow()!=-1) {
                                                String row = classes.model.getValueAt(classes.table1.getSelectedRow(), 0).toString();
                                                System.out.println(row);
                                                int t= Integer.parseInt(row);
                                                System.out.println(t);
                                                try {
                                                    String reponce=server.deleteClasse(row);
                                                    if(reponce.equals("ok"))
                                                    JOptionPane.showMessageDialog(null, "classe deleted successfully");
                                                    else
                                                        JOptionPane.showMessageDialog(null, "\n" +
                                                                "operation failed ,class contains students");
                                                } catch (RemoteException ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        }

                                    });
                                    classes.refresh.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            try {
                                                classes.getClasses(server.getClasses());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }

                                        }
                                    });
                                    classes.add.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            Add_classe add_classe= null;
                                            try {
                                                add_classe = new Add_classe(server.getTeachersName());
                                            } catch (RemoteException ex) {
                                                ex.printStackTrace();
                                            }
                                            Add_classe finalAdd_classe = add_classe;
                                            Add_classe finalAdd_classe1 = add_classe;
                                            add_classe.addButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {

                                                    String name= finalAdd_classe.username.getText().trim();
                                                    String teacher= (String) finalAdd_classe.comboBox1.getSelectedItem();

                                                    try {

                                                            String reponce = server.addClasse(name,teacher);
                                                            if (reponce.equals("ok")) {
                                                                System.out.println("ok");
                                                                finalAdd_classe1.error.setText("classe added successfully");
                                                                finalAdd_classe1.username.setText("");
                                                                finalAdd_classe1.comboBox1.setSelectedItem("");


                                                            }
                                                            else{
                                                                finalAdd_classe1.error.setText("already exist");
                                                            }
                                                        }



                                                    catch (RemoteException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            });
                                            classes.update.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    String row = "";
                                                    if (classes.table1.getSelectedRow()!=-1) {
                                                        row = classes.model.getValueAt(classes.table1.getSelectedRow(), 0).toString();
                                                        System.out.println(row);

                                                        try {
                                                            ArrayList<String> liste=server.getClasse(row);
                                                            ArrayList<String> teachers_names=server.getTeachersName();
                                                            Update_classe update_classe=new Update_classe(teachers_names);
                                                            update_classe.username.setText(liste.get(0));
                                                            update_classe.comboBox1.setSelectedItem(liste.get(1));

                                                            String finalRow = row;
                                                            update_classe.updateButton.addActionListener(new ActionListener() {
                                                                @Override
                                                                public void actionPerformed(ActionEvent e) {
                                                                    String classeName=update_classe.username.getText().trim();
                                                                    String teacher_name= (String) update_classe.comboBox1.getSelectedItem();




                                                                        String reponce = null;
                                                                        try {
                                                                            reponce = server.updateClasse(finalRow, classeName,teacher_name);
                                                                        } catch (RemoteException ex) {
                                                                            ex.printStackTrace();
                                                                        }
                                                                        if (reponce.equals("ok")) {
                                                                            System.out.println("ok");
                                                                            update_classe.error.setText("classe updated successfully");
                                                                            update_classe.username.setText("");
                                                                            update_classe.comboBox1.setSelectedItem("");

                                                                        } else {
                                                                            update_classe.error.setText("teacher already affected or classe exists");
                                                                        }


                                                                }
                                                            });


                                                        } catch (RemoteException ex) {
                                                            ex.printStackTrace();
                                                        }
                                                    }}

                                            });
                                        }
                                    });
                                } catch (RemoteException ex) {
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

    @Override
    public void sendfile(File file) throws RemoteException {

    }

    @Override
    public void receive_message(String message) throws RemoteException {

    }

    @Override
    public void receive_file(List<Integer> data, String file) throws RemoteException {

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

    @Override
    public void send_shape(String operation, ArrayList<Point> points) throws RemoteException {

    }

    @Override
    public void receive_shape(String operation, ArrayList<Point> points) throws RemoteException {

    }


    public void register(JFrame frame)throws RemoteException {
        Register register=new Register();
        Admin admin=this;

        register.registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName=register.username.getText().trim();
                password=register.password.getText().trim();
                 email=register.email.getText().trim();
               String confirmed=register.confirmed.getText().trim();
                try {
               if(confirmed.equals(password)){
                   String reponce = server.register(admin,"admin");
                   if (reponce.equals("ok")) {
                       System.out.println("ok");
                       register.dispose();
                       login(frame);

                   }
                   else{
                       register.error.setText("already exist");
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
}
