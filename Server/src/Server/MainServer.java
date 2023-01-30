package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.SQLException;

public class MainServer {
    public static void main(String[] args) {
        try {
            Server server = new ServerImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("learning", server);
            System.out.println("Server running on port 1099");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
