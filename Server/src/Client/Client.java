package Client;

import java.awt.*;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Client extends Remote {
    void sendfile(File file) throws RemoteException;

    void receive_message(String message) throws RemoteException;

    void receive_file(List<Integer> data, String file) throws RemoteException;

    String getUsername() throws RemoteException;

    String getPassword() throws RemoteException;

    String getEmail() throws RemoteException;

    void send_shape(String operation, ArrayList<Point> points) throws RemoteException;

    void receive_shape(String operation, ArrayList<Point> points) throws RemoteException;
}
