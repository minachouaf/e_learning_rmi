package Server;


import Client.Client;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface Server extends Remote {
    public String login(Client client,String instance) throws RemoteException;
    public String register(Client client,String instance) throws RemoteException;

    public Object[][] getTeachers()throws RemoteException;
    public ArrayList<String> getTeachersName()throws RemoteException;
    public String deleteTeacher(String row)throws RemoteException;
    String addTeacher(String username,String email,String password) throws  RemoteException;
    ArrayList<String> getteacher(String row) throws RemoteException;
    String updateTeacher(String row,String username,String email,String password) throws  RemoteException;



    public Object[][] getStudents()throws RemoteException;

    public String deleteStudent(String row)throws RemoteException;
    String addStudent(String username,String email,String password) throws  RemoteException;
    ArrayList<String> getStudent(String row) throws RemoteException;
    String updateStudent(String row,String username,String email,String password) throws  RemoteException;
    public ArrayList<String> getStudentsName()throws RemoteException;
    public ArrayList<String> getClassesName()throws RemoteException;
    public Object[][] getClasses()throws RemoteException;
    public String deleteClasse(String row)throws RemoteException;
    public String addClasse(String classename,String teacher) throws  RemoteException;
    public ArrayList<String> getClasse(String row) throws RemoteException;
    public String updateClasse(String row,String classename,String teacher) throws  RemoteException;

    public Object[][] getInscriptions()throws RemoteException;
    public String deleteInscription(String row)throws RemoteException;
    public String addInscription(String classename,String teacher) throws  RemoteException;
    public ArrayList<String> getInscription(String row) throws RemoteException;
    public String updateInscription(String row,String classename,String teacher) throws  RemoteException;
    public String getClassbyStudentname(String studentname) throws RemoteException;

    public void transfer_message(List destination, String message) throws RemoteException;
    void transfer_file(List<String> destination,String file,List<Integer> data) throws RemoteException;
    public List<String> getConnectedstudent(String userName) throws RemoteException;

    void transfer_shape_student(String operation, ArrayList<Point> points,String username) throws RemoteException;
    String getClasse_teacher(String nameclasse) throws RemoteException;

    public void transfer_shape(String operation, ArrayList<Point> points,String name) throws RemoteException;
    String getClassbyTeachername(String userName) throws RemoteException;

    public ArrayList<String> getConnected_teacher_student(String classeName) throws RemoteException;
    void logout_student(String username) throws RemoteException;
    void logout_teacher(String username) throws RemoteException;

    ArrayList<String> get_teacher_byname(String teacher) throws RemoteException;
    ArrayList<String> get_student_byname(String student) throws RemoteException;
    ArrayList<String> get_admin_byname(String admin) throws RemoteException;
    String updateStudent(String row, String note) throws RemoteException;
    Object[][] getStudents_byteacher(String name) throws RemoteException;
}
