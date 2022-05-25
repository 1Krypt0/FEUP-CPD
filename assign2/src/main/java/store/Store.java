package store;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;

public class Store implements IStore {

    private final MulticastDispatcher multicastDispatcher;
    private final TCPDispatcher tcpDispatcher;

    // TODO: Change to file for persistent storage
    public int membershipCounter;

    // Thread Pool -> Launches and terminates threads
    // Launches one thread to listen to messages, which in itself launches a thread
    // per message received
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Correct usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port> ");
        }

    }

    public Store(String[] args) throws IOException {
        this.multicastDispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        this.tcpDispatcher = new TCPDispatcher(Integer.parseInt(args[3]), this);
        this.membershipCounter = -1;
    }

    private byte[] getHash(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value);
    }

    @Override
    public void put(byte[] key, String value) throws NoSuchAlgorithmException {
        System.out.println(getHash(key));
    }

    @Override
    public String get(byte[] key) {
        return null;
    }

    @Override
    public void delete(byte[] key) {

    }
}
