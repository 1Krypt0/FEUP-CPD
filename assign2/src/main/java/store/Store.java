package store;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import communication.messages.MembershipMessage;

public class Store implements IStore {

    private final int nodeID;

    private final MulticastDispatcher multicastDispatcher;
    private final TCPDispatcher tcpDispatcher;

    private final List<Integer> clusterIDs;

    private final StorageManager storeManager;

    private final HashMap<Integer, Integer> clusterPorts;

    // TODO: Change to file for persistent storage
    public int membershipCounter;

    // Thread Pool -> Launches and terminates threads
    // Launches one thread to listen to messages, which in itself launches a thread
    // per message received
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Correct usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port> ");
        }

        try {
            Store store = new Store(args);

            store.startDispatchers();
        } catch (IOException e) {
            System.out.println("Error using store: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Store(String[] args) throws IOException {
        this.multicastDispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        this.nodeID = Integer.parseInt(args[2]);
        this.tcpDispatcher = new TCPDispatcher(Integer.parseInt(args[3]), this);
        this.membershipCounter = -1;
        this.clusterIDs = new ArrayList<>();
        this.clusterPorts = new HashMap<>();
        this.storeManager = new StorageManager(this.nodeID);
        clusterIDs.add(this.nodeID);
    }

    private void startDispatchers() {
        Thread multicastDispatcherThread = new Thread(multicastDispatcher);
        multicastDispatcherThread.start();

        Thread tcpDispatcherThread = new Thread(tcpDispatcher);
        tcpDispatcherThread.start();
    }

    public void addNewNode(String[] msgHeader) throws InterruptedException {
        // NOTE: Things to do
        // add to cluster id - DONE
        // Update membership log - DONE
        // await random time length - DONE
        // build membership message - DONE
        // send membership message - DONE
        final int newNodeID = Integer.parseInt(msgHeader[1]);
        final int membershipCounter = Integer.parseInt(msgHeader[2]);
        final int newNodePort = Integer.parseInt(msgHeader[3]);

        // Update memberships
        clusterIDs.add(newNodeID);
        Collections.sort(clusterIDs);

        // Add port
        clusterPorts.put(newNodeID, newNodePort);

        storeManager.updateMembershipLog(newNodeID, membershipCounter);

        Thread.sleep((long) Math.random() * 1000);

        byte[] message = MembershipMessage.buildMessage(clusterIDs.toString(), storeManager.getMembershipLog());

        tcpDispatcher.sendMessage(message);
    }

    @Override
    public void put(byte[] key, String value) throws NoSuchAlgorithmException {

    }

    @Override
    public String get(byte[] key) {
        return "";
    }

    @Override
    public void delete(byte[] key) {

    }

}
