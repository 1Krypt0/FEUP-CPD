package communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
    String join() throws RemoteException;

    String leave() throws RemoteException;

    String put(String fileName) throws RemoteException;

    String get(String key) throws RemoteException;

    String delete(String key) throws RemoteException;
}
