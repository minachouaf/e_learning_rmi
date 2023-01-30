package Server;



import Client.Client;


import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ServerImpl extends UnicastRemoteObject implements Server {
    private static final long serialVersionUID = 1L;
    private ArrayList<Client> connected_Teacher;
    private ArrayList<Client> connected_Student;


    private Connection connection;

    protected ServerImpl() throws RemoteException, SQLException {
        super();
        connected_Student = new ArrayList<>();
        connected_Teacher = new ArrayList<>();

        Connexion_db con = Connexion_db.getInstance();
        con.connectToDatabase();
        connection = con.getConnection();
    }

    @Override
    public String login(Client client, String instance) throws RemoteException {
        try {
            if (instance.equals("admin")) {

                PreparedStatement ps = connection.prepareStatement("SELECT username,password FROM admin WHERE username=? AND password =?");

                ps.setString(1, client.getUsername());


                ps.setString(2, client.getPassword());


                ResultSet rs = ps.executeQuery();



                if (rs.next()) {

                    System.out.println("connected");
                    return "connected";

                } else {
                    System.out.println("refused");
                    return "refused";
                }

            } else if (instance.equals("student")) {
                PreparedStatement ps = connection.prepareStatement("SELECT username,password FROM student WHERE username=? AND password =?");

                ps.setString(1, client.getUsername());


                ps.setString(2, client.getPassword());


                ResultSet rs = ps.executeQuery();
                boolean exist=false;
                for(int i=0;i<connected_Student.size();i++)
                {
                    if(connected_Student.get(i).getUsername().equals(client.getUsername()))
                    {
                        exist=true;
                    }
                }

                if (rs.next() && !exist) {
                    connected_Student.add(client);
                    System.out.println("connected");
                    return "connected";

                } else {
                    System.out.println("refused");
                    return "refused";
                }
            } else {
                PreparedStatement ps = connection.prepareStatement("SELECT username,password FROM teacher WHERE username=? AND password =?");

                ps.setString(1, client.getUsername());


                ps.setString(2, client.getPassword());


                ResultSet rs = ps.executeQuery();
                boolean exist=false;
                for(int i=0;i<connected_Teacher.size();i++)
                {
                    if(connected_Teacher.get(i).getUsername().equals(client.getUsername()))
                    {
                        exist=true;
                    }
                }

                if (rs.next() && !exist) {


                    connected_Teacher.add(client);
                    System.out.println("connected");
                    return "connected";

                } else {
                    System.out.println("refused");
                    return "refused";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return "";
    }

    @Override
    public String register(Client client, String instance) throws RemoteException {
        try {
            if (instance.equals("admin")) {
                PreparedStatement ps = connection.prepareStatement("SELECT username FROM admin WHERE UserName=? ");

                ps.setString(1, client.getUsername());


                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {

                    PreparedStatement r = connection.prepareStatement("insert into admin(username,email,password) values (?,?,?)");
                    r.setString(1, client.getUsername());
                    r.setString(2, client.getEmail());
                    r.setString(3, client.getPassword());

                    int row = r.executeUpdate();


                    return "ok";
                } else return "non ok";
            } else if (instance.equals("student")) {
                PreparedStatement ps = connection.prepareStatement("SELECT username FROM student WHERE UserName=? ");

                ps.setString(1, client.getUsername());


                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {

                    PreparedStatement r = connection.prepareStatement("insert into student(username,email,password) values (?,?,?)");
                    r.setString(1, client.getUsername());
                    r.setString(2, client.getEmail());
                    r.setString(3, client.getPassword());

                    int row = r.executeUpdate();


                    return "ok";
                } else return "non ok";
            } else {
                PreparedStatement ps = connection.prepareStatement("SELECT username FROM teacher WHERE UserName=? ");

                ps.setString(1, client.getUsername());


                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {

                    PreparedStatement r = connection.prepareStatement("insert into teacher(username,email,password) values (?,?,?)");
                    r.setString(1, client.getUsername());
                    r.setString(2, client.getEmail());
                    r.setString(3, client.getPassword());

                    int row = r.executeUpdate();


                    return "ok";
                } else return "non ok";
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    @Override
    public Object[][] getTeachers() throws RemoteException {

        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT * FROM teacher ");
            int size = 0;
            if (rs.last()) {

                size = rs.getRow();
            }
            Object[][] liste = new Object[size][4];

            System.out.println(liste.toString());
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                liste[i][0] = rs.getInt("id");

                liste[i][1] = rs.getString("username");

                liste[i][2] = rs.getString("email");

                liste[i][3] = rs.getString("password");

                i++;
            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[8][];

    }

    @Override
    public String deleteTeacher(String row) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT id_teacher FROM classes WHERE id_teacher=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                r = connection.prepareStatement("delete from teacher where id=?");
                r.setInt(1, Integer.parseInt(row));
                int resultat = r.executeUpdate();
                return "ok";
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";

    }

    @Override
    public String addTeacher(String username, String email, String password) throws RemoteException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT username FROM teacher WHERE UserName=? ");


            ps.setString(1, username);


            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {

                PreparedStatement r = connection.prepareStatement("insert into teacher(username,email,password) values (?,?,?)");
                r.setString(1, username);
                r.setString(2, email);
                r.setString(3, password);

                int row = r.executeUpdate();


                return "ok";
            } else return "non ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getteacher(String row) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT username,email,password FROM teacher WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                 liste.add(rs.getString("username"));

                liste.add(rs.getString("email"));

                liste.add(rs.getString("password"));
            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public String updateTeacher(String row, String username, String email, String password) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {

                ps = connection.prepareStatement("SELECT username FROM teacher WHERE id=? ");


                ps.setInt(1, Integer.parseInt(row));


                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    r = connection.prepareStatement("UPDATE teacher set username=?,email=?,password=? where id=?");

                    r.setString(1, username);
                    r.setString(2, email);
                    r.setString(3, password);
                    r.setInt(4, Integer.parseInt(row));

                    int resultat = r.executeUpdate();


                    return "ok";

                }} catch (SQLException e) {
        e.printStackTrace();
    }
        return "no ok";
    }

    @Override
    public ArrayList<String> getTeachersName() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT username FROM teacher ");

            ArrayList<String> liste=new ArrayList<>();


            while (rs.next()) {



                liste.add(rs.getString("username"));


            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    @Override
    public Object[][] getStudents_byteacher(String name) throws RemoteException {
        PreparedStatement ps = null;
        try {

            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
           /* ps = connection.prepareStatement("SELECT s.id ,s.username,s.email,s.note FROM student s,teacher t,classes c,inscription i WHERE  s.id=i.id_student and i.id_classe=c.id and  t.id=c.id_teacher and t.username=?");
            ps.setString(1,"name");
            ResultSet rs = ps.executeQuery();

            */

            ResultSet rs = stmt.executeQuery("SELECT s.id ,s.username,s.email,s.note FROM student s,teacher t,classes c,inscription i WHERE  s.id=i.id_student and i.id_classe=c.id and  t.id=c.id_teacher and t.username='"+name+"'  ");


            int size = 0;
            if (rs.last()) {

                size = rs.getRow();
            }
            Object[][] liste = new Object[size][4];

            System.out.println(liste.toString());
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                liste[i][0] = rs.getInt("id");

                liste[i][1] = rs.getString("username");

                liste[i][2] = rs.getString("email");

                liste[i][3] = rs.getString("note");

                i++;
            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[8][];

    }

    @Override
    public Object[][] getStudents() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT * FROM student ");
            int size = 0;
            if (rs.last()) {

                size = rs.getRow();
            }
            Object[][] liste = new Object[size][4];

            System.out.println(liste.toString());
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                liste[i][0] = rs.getInt("id");

                liste[i][1] = rs.getString("username");

                liste[i][2] = rs.getString("email");

                liste[i][3] = rs.getString("password");


                i++;
            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[8][];

    }
    @Override
    public String deleteStudent(String row) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT id_student FROM inscription WHERE id_student=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
            r = connection.prepareStatement("delete from student where id=?");
            r.setInt(1, Integer.parseInt(row));
            int resultat = r.executeUpdate();
            return "ok";
        }} catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";
    }

    @Override
    public String addStudent(String username, String email, String password) throws RemoteException {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("SELECT username FROM student WHERE UserName=? ");


            ps.setString(1, username);


            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {

                PreparedStatement r = connection.prepareStatement("insert into student(username,email,password) values (?,?,?)");
                r.setString(1, username);
                r.setString(2, email);
                r.setString(3, password);

                int row = r.executeUpdate();


                return "ok";
            } else return "non ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> getStudent(String row) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT username,email,password,note FROM student WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                liste.add(rs.getString("username"));

                liste.add(rs.getString("email"));

                liste.add(rs.getString("password"));
                liste.add(rs.getString("note"));
            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public String updateStudent(String row, String note) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {

            ps = connection.prepareStatement("SELECT username FROM student WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                r = connection.prepareStatement("UPDATE student set note=? where id=?");

                r.setDouble(1, Double.parseDouble(note));

                r.setInt(2, Integer.parseInt(row));

                int resultat = r.executeUpdate();


                return "ok";

            }} catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";
    }

    @Override
    public String updateStudent(String row, String username, String email, String password) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {

            ps = connection.prepareStatement("SELECT username FROM student WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                r = connection.prepareStatement("UPDATE student set username=?,email=?,password=? where id=?");

                r.setString(1, username);
                r.setString(2, email);
                r.setString(3, password);
                r.setInt(4, Integer.parseInt(row));

                int resultat = r.executeUpdate();


                return "ok";

            }} catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";
    }

    @Override
    public ArrayList<String> getStudentsName() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT username FROM student ");

            ArrayList<String> liste=new ArrayList<>();


            while (rs.next()) {



                liste.add(rs.getString("username"));


            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    @Override
    public ArrayList<String> getClassesName() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT Name_classe FROM classes ");

            ArrayList<String> liste=new ArrayList<>();


            while (rs.next()) {



                liste.add(rs.getString("Name_classe"));


            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    @Override
    public Object[][] getClasses() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            //ps = connection.prepareStatement("SELECT * FROM teacher ");


            ResultSet rs = stmt.executeQuery("SELECT * FROM classes ");
            int size = 0;
            if (rs.last()) {

                size = rs.getRow();
            }
            Object[][] liste = new Object[size][3];

            System.out.println(liste.toString());
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                liste[i][0] = rs.getInt("id");
                liste[i][1] = rs.getString("Name_classe");
               int teacher=  rs.getInt("id_teacher");
                ArrayList<String> teacher_data=getteacher(String.valueOf(teacher));
                String teacher_name=teacher_data.get(0);
                liste[i][2] = teacher_name;
                i++;
            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[8][];

    }

    @Override
    public String deleteClasse(String row) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("select id_classe from inscription where id_classe=?");
            ps.setInt(1, Integer.parseInt(row));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                r = connection.prepareStatement("delete from classes where id=?");
                r.setInt(1, Integer.parseInt(row));
                int resultat = r.executeUpdate();
                return "ok";
            }
            else{
                return "no ok";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String addClasse(String classename,String teacher) throws  RemoteException{
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        try {
            ps = connection.prepareStatement("SELECT Name_classe FROM classes WHERE Name_classe=? ");


            ps.setString(1, classename);


            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                ps1 = connection.prepareStatement("SELECT id FROM teacher WHERE username=? ");


                ps1.setString(1, teacher);


                ResultSet resultat = ps1.executeQuery();
                if (resultat.next()) {
                int idteacher=resultat.getInt("id");
                PreparedStatement r = connection.prepareStatement("insert into classes(Name_classe,id_teacher) values (?,?)");
                r.setString(1, classename);
                r.setInt(2, idteacher);


                int row = r.executeUpdate();


                return "ok";
            }} else return "non ok";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "non ok";
    }

    @Override
    public ArrayList<String> getClasse(String row) throws RemoteException {
        PreparedStatement ps = null;

        ArrayList<String> liste=new ArrayList<>();
        try {



            ps = connection.prepareStatement("SELECT Name_classe,id_teacher FROM classes WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                liste.add(rs.getString("Name_classe"));
                int id_teacher=rs.getInt("id_teacher");
                ArrayList<String> data_teacher=getteacher(String.valueOf(id_teacher));
                liste.add(data_teacher.get(0));

            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public String updateClasse(String row,String classename,String teacher)  throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        PreparedStatement ps1;
        try {


            ps1 = connection.prepareStatement("SELECT id_teacher FROM classes WHERE id=? and  id_teacher not in (select id_teacher from classes where id!=?)");


            ps1.setInt(1, Integer.parseInt(row));
            ps1.setInt(2, Integer.parseInt(row));


            ResultSet resultSet = ps1.executeQuery();

            if (resultSet.next()) {
            ps = connection.prepareStatement("SELECT Name_classe FROM classes WHERE id=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                ps1 = connection.prepareStatement("SELECT id FROM teacher WHERE username=? ");
                ps1.setString(1, teacher);
                ResultSet resultat = ps1.executeQuery();

                if (resultat.next()) {
                    int idteacher=resultat.getInt("id");
                r = connection.prepareStatement("UPDATE classes set Name_classe=?,id_teacher=? where id=?");

                r.setString(1, classename);
                r.setInt(2, idteacher);

                r.setInt(3, Integer.parseInt(row));

                int result= r.executeUpdate();


                return "ok";

            }}}} catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";

    }

    @Override
    public Object[][] getInscriptions() throws RemoteException {
        PreparedStatement ps = null;
        try {
            Statement stmt = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);



            ResultSet rs = stmt.executeQuery("SELECT * FROM inscription ");
            int size = 0;
            if (rs.last()) {

                size = rs.getRow();
            }
            Object[][] liste = new Object[size][3];

            System.out.println(liste.toString());
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {

                liste[i][0] = rs.getInt("id_inscription");
                int student_id = rs.getInt("id_student");
                int classe_id=  rs.getInt("id_classe");
                ArrayList<String> classe_data=getClasse(String.valueOf(classe_id));
                String classe_name=classe_data.get(0);
                liste[i][2] = classe_name;
                ArrayList<String> student_data=getStudent(String.valueOf(student_id));
                String student_name=student_data.get(0);
                liste[i][1] = student_name;
                i++;
            }

            return liste;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[8][];

    }

    @Override
    public String deleteInscription(String row) throws RemoteException {
        PreparedStatement r = null;

        try {

                r = connection.prepareStatement("delete from inscription where id_inscription=?");
                r.setInt(1, Integer.parseInt(row));
                int resultat = r.executeUpdate();
                return "ok";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public String addInscription(String student, String classe) throws RemoteException {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2;
        try {

                ps1 = connection.prepareStatement("SELECT id FROM student WHERE username=? ");


                ps1.setString(1, student);


                ResultSet resultat = ps1.executeQuery();
            int idstudent = 0;
                if (resultat.next()) {
                    idstudent = resultat.getInt("id");
                }

            ps = connection.prepareStatement("SELECT id FROM classes WHERE Name_classe=? ");


            ps.setString(1, classe);


            ResultSet result = ps.executeQuery();
            int idclasse = 0;
            if (result.next()) {
                idclasse = result.getInt("id");
            }
            ps2 = connection.prepareStatement("SELECT id_student FROM inscription WHERE id_student=? ");


            ps2.setInt(1, idstudent);


            ResultSet test = ps2.executeQuery();
            if(!test.next()){
                    PreparedStatement r = connection.prepareStatement("insert into inscription(id_student,id_classe) values (?,?)");
                    r.setInt(1, idstudent);
                    r.setInt(2, idclasse);


                    int row = r.executeUpdate();


                    return "ok";

        }} catch (SQLException e) {
            e.printStackTrace();
        }
        return "non ok";
    }

    @Override
    public ArrayList<String> getInscription(String row) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT id_student,id_classe FROM inscription WHERE id_inscription=? ");


            ps.setInt(1, Integer.parseInt(row));


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id_student=rs.getInt("id_student");
                int id_classe=rs.getInt("id_classe");
                ArrayList<String> data_student=getStudent(String.valueOf(id_student));
                liste.add(data_student.get(0));
                ArrayList<String> data_classe=getClasse(String.valueOf(id_classe));
                liste.add(data_classe.get(0));

            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }


    @Override
    public String updateInscription(String row, String studentname, String classename) throws RemoteException {
        PreparedStatement r = null;
        PreparedStatement ps = null;
        PreparedStatement ps1;

        try {


            ps1 = connection.prepareStatement("SELECT id_student FROM inscription WHERE id_inscription=? and  id_student not in (select id_student from inscription where id_inscription!=?)");


            ps1.setInt(1, Integer.parseInt(row));
            ps1.setInt(2, Integer.parseInt(row));


            ResultSet resultSet = ps1.executeQuery();

            if (resultSet.next()) {





            ps1 = connection.prepareStatement("SELECT id FROM student WHERE username=? ");


            ps1.setString(1, studentname);


            ResultSet resultat = ps1.executeQuery();
            int idstudent = 0;
            if (resultat.next()) {
                idstudent = resultat.getInt("id");
            }

            ps = connection.prepareStatement("SELECT id FROM classes WHERE Name_classe=? ");


            ps.setString(1, classename);


            ResultSet result = ps.executeQuery();
            int idclasse = 0;
            if (result.next()) {
                idclasse = result.getInt("id");
            }





                    r = connection.prepareStatement("UPDATE inscription set id_student=?,id_classe=? where id_inscription=?");

                    r.setInt(1, idstudent);
                    r.setInt(2, idclasse);

                    r.setInt(3, Integer.parseInt(row));

                    int ress= r.executeUpdate();


                    return "ok";

                 }}catch (SQLException e) {
            e.printStackTrace();
        }
        return "no ok";

    }

    @Override
    public String getClassbyStudentname(String studentname) throws RemoteException {
        PreparedStatement ps1;

        try {


            ps1 = connection.prepareStatement("SELECT Name_classe FROM inscription i,classes c,student s WHERE   s.id=i.id_student and i.id_classe=c.id and s.username=?");


            ps1.setString(1, studentname);



            ResultSet resultSet = ps1.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("Name_classe");
            }

            } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void transfer_message(List destination, String message) throws RemoteException {



     String tech="";

        for(int i=0;i<connected_Student.size();i++){
            for (int j=0;j<destination.size();j++)
            {
                if(((String) destination.get(j)).split("_")[0].equals("T") ) {
                    tech = ((String) destination.get(j)).split("_")[1];
                    System.out.println(tech);
                }



                if(destination.get(j).equals(connected_Student.get(i).getUsername())){

                    connected_Student.get(i).receive_message(message);

                }
            }
        }
        System.out.println(tech);

        for (int j=0;j<connected_Teacher.size();j++){

            if(tech.equals(connected_Teacher.get(j).getUsername())) {

                connected_Teacher.get(j).receive_message(message);

            }
        }
    }

    @Override
    public void transfer_file(List<String> destination, String file,List<Integer> data) throws RemoteException {
        String tech="";
        for(int i=0;i<connected_Student.size();i++){
            for (int j=0;j<destination.size();j++)
            {
                if(((String) destination.get(j)).split("_")[0].equals("T") ) {
                    tech = ((String) destination.get(j)).split("_")[1];
                    System.out.println(tech);
                }
                if(destination.get(j).equals(connected_Student.get(i).getUsername())){
                    connected_Student.get(i).receive_file(data,file);

                }
            }
        }
        System.out.println(tech);

        for (int j=0;j<connected_Teacher.size();j++){

            if(tech.equals(connected_Teacher.get(j).getUsername())) {

                connected_Teacher.get(j).receive_file(data,file);

            }
        }
    }

    @Override
    public List<String> getConnectedstudent(String userName) throws RemoteException {
        ArrayList<String> liste = new ArrayList<>();
        ArrayList<String> liste1 = new ArrayList<>();
        PreparedStatement ps1;

        try {

            String nameClasse=getClassbyStudentname(userName);
            ps1 = connection.prepareStatement("SELECT username FROM inscription i,classes c,student s WHERE   s.id=i.id_student and i.id_classe=c.id and c.Name_classe=? and s.username!=?");


            ps1.setString(1, nameClasse);
            ps1.setString(2, userName);


            ResultSet resultSet = ps1.executeQuery();
                  for(int i=0;i<connected_Student.size();i++){
                      liste1.add(connected_Student.get(i).getUsername());
                  }
            while (resultSet.next()) {
                 if(liste1.contains(resultSet.getString("username")))
                liste.add(resultSet.getString("username"));
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return liste;
        }

    }

    public String getTeacher(String student){
        String teacher="";
        PreparedStatement ps1;

        try {


            ps1 = connection.prepareStatement("SELECT t.username FROM inscription i,classes c,teacher t,student s WHERE s.id=i.id_student and i.id_classe=c.id and t.id=c.id_teacher and s.username=?  ");


            ps1.setString(1, student);
            ResultSet resultSet = ps1.executeQuery();
            if(resultSet.next()){
                teacher=resultSet.getString("username") ;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return teacher;
        }

    }
        @Override
    public void transfer_shape(String operation, ArrayList<Point> points,String name) throws RemoteException {
            ArrayList<String> liste=getConnected_teacher_student(name);

            for(int i=0;i<connected_Student.size();i++){
                if(liste.contains(connected_Student.get(i).getUsername()))
                    connected_Student.get(i).receive_shape(operation,points);

            }



    }

    @Override
    public String getClasse_teacher(String nameclasse) throws RemoteException {
        PreparedStatement ps1;
        String teacher = "";
        ArrayList<String> liste1=new ArrayList<>();

        try {


            ps1 = connection.prepareStatement("SELECT username FROM classes c,teacher t WHERE   t.id=c.id_teacher and  c.Name_classe=?");


            ps1.setString(1, nameclasse);


            ResultSet resultSet = ps1.executeQuery();
            for(int i=0;i<connected_Teacher.size();i++){
                liste1.add(connected_Teacher.get(i).getUsername());
            }

            if (resultSet.next()) {
                if(liste1.contains(resultSet.getString("username")))
               teacher=resultSet.getString("username");
            }

    } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            return teacher;
        }
        }

    @Override
    public void transfer_shape_student(String operation, ArrayList<Point> points,String name) throws RemoteException {
        String teacher=getTeacher(name);
        for (int j=0;j<connected_Teacher.size();j++){
            if(connected_Teacher.get(j).getUsername().equals(teacher))
                connected_Teacher.get(j).receive_shape(operation,points);
        }
        ArrayList<String> liste= (ArrayList<String>) getConnectedstudent(name);
        for(int i=0;i<connected_Student.size();i++){
            if(liste.contains(connected_Student.get(i).getUsername()))
            connected_Student.get(i).receive_shape(operation,points);

        }
    }

    @Override
    public ArrayList<String> getConnected_teacher_student(String username) throws RemoteException {
        ArrayList<String> liste = new ArrayList<>();
        ArrayList<String> liste1 = new ArrayList<>();
        PreparedStatement ps1;

        try {


            ps1 = connection.prepareStatement("SELECT s.username FROM inscription i,classes c,student s,teacher t WHERE  s.id=i.id_student and i.id_classe=c.id and  t.id=c.id_teacher and t.username=? ");


            ps1.setString(1, username);



            ResultSet resultSet = ps1.executeQuery();
            for (int i = 0; i < connected_Student.size(); i++) {
                liste1.add(connected_Student.get(i).getUsername());
            }
            while (resultSet.next()) {
                if (liste1.contains(resultSet.getString("username")))
                    liste.add(resultSet.getString("username"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return liste;
        }
    }

    @Override
    public String getClassbyTeachername(String userName) throws RemoteException {
        PreparedStatement ps1;

        try {


            ps1 = connection.prepareStatement("SELECT Name_classe FROM classes c,teacher t WHERE   t.id=c.id_teacher  and t.username=?");


            ps1.setString(1, userName);



            ResultSet resultSet = ps1.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("Name_classe");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public ArrayList<String> get_teacher_byname(String teacher) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT id,username,email,password FROM teacher WHERE username=? ");


            ps.setString(1, teacher);


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                liste.add(rs.getString("username"));

                liste.add(rs.getString("email"));

                liste.add(rs.getString("password"));
                liste.add(String.valueOf(rs.getInt("id")));
            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;

    }

    @Override
    public ArrayList<String> get_student_byname(String student) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT id,username,email,password,note FROM student WHERE username=? ");


            ps.setString(1, student);


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                liste.add(rs.getString("username"));

                liste.add(rs.getString("email"));

                liste.add(rs.getString("password"));
                liste.add(String.valueOf(rs.getInt("id")));
                liste.add(String.valueOf(rs.getDouble("note")));
            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public ArrayList<String> get_admin_byname(String admin) throws RemoteException {
        PreparedStatement ps = null;
        ArrayList<String> liste=new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT id,username,email,password FROM admin WHERE username=? ");


            ps.setString(1, admin);


            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                liste.add(rs.getString("username"));

                liste.add(rs.getString("email"));

                liste.add(rs.getString("password"));
                liste.add(String.valueOf(rs.getInt("id")));
            }
            return liste;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    @Override
    public void logout_student(String username) throws RemoteException {
        for (int i=0;i<connected_Student.size();i++){
            if(connected_Student.get(i).getUsername().equals(username)){
                connected_Student.remove(i);
            }
        }
    }

    @Override
    public void logout_teacher(String username) throws RemoteException {
        for (int i=0;i<connected_Teacher.size();i++){
            if(connected_Teacher.get(i).getUsername().equals(username)){
                connected_Teacher.remove(i);
            }
        }
    }

}