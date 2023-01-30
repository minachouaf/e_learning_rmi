package Client.windows;

import Client.Admin.Admin;

import Client.Student.Student;
import Client.Teacher.Teacher;
import Server.Server;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Home {
    private JPanel main;
    private JButton register;
    private JButton login;
    private JComboBox comboBox1;
    private JPanel tools;
    private JPanel pan3;
    Server server;
    JFrame frame;
    public Home() throws MalformedURLException, NotBoundException, RemoteException {
        frame=new JFrame();
        register.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/register1.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        login.setIcon(new ImageIcon(new ImageIcon(ClassLoader.getSystemResource("images/login.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        login.setBorderPainted(false);
        register.setBorderPainted(false);
        String url = "rmi://localhost/learning";
        server = (Server) Naming.lookup(url);
        login.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (comboBox1.getSelectedItem().equals("Admin")) {
                        try {
                            Admin admin = new Admin(server);
                            admin.login(frame);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }

                    else if (comboBox1.getSelectedItem().equals("Student")) {
                        try {
                            Student student = new Student(server);
                            student.login(frame);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        try {
                            Teacher teacher = new Teacher(server);
                            teacher.login(frame);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }

                    }
                }

            });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox1.getSelectedItem().equals("Admin")) {
                    try {
                        Admin admin = new Admin(server);
                        admin.register(frame);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
                else if (comboBox1.getSelectedItem().equals("Student")) {
                    try {
                        Student student = new Student(server);
                        student.register(frame);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    try {
                        Teacher teacher = new Teacher(server);
                        teacher.register(frame);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }

                }
            }

        });

            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(main);
            frame.setVisible(true);
            frame.setSize(1000, 500);
            frame.setTitle("E_learning");
            frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource("images/icon2.png")).getImage());

    }

    public static void main(String[] args) {
        try {
            Home home=new Home();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    }
