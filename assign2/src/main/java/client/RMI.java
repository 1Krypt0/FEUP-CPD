package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {
    void put(byte[] key, String value) throws RemoteException; // TODO: 4/28/22 Change void type to actual type

    String get(byte[] key) throws RemoteException;

    void delete(byte[] key) throws RemoteException;
}
