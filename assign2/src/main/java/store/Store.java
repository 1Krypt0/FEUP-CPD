package store;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import communication.MulticastDispatcher;
import utils.Utils;

public class Store implements IStore {

    private final MulticastDispatcher dispatcher;

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
        this.dispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        this.membershipCounter = -1;
    }

    @Override
    public void put(byte[] key, String value) throws NoSuchAlgorithmException {
        System.out.println(Utils.getHash(key));
    }

    @Override
    public String get(byte[] key) {
        return null;
    }

    @Override
    public void delete(byte[] key) {

    }
}
