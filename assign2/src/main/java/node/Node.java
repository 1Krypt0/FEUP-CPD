package node;

import client.RMI;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Node implements RMI, Serializable {
    @Override
    public void put(byte[] key, String value) throws RemoteException {

    }

    @Override
    public String get(byte[] key) throws RemoteException {
        return null;
    }

    @Override
    public void delete(byte[] key) throws RemoteException {

    }
}
