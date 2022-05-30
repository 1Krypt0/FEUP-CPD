package store;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    private static final String LOCALHOST = "localhost";

    private final int nodeID;

    private final HashMap<Integer, String> clusterIPs;
    private final HashMap<Integer, Integer> clusterPorts;
    private final List<Integer> clusterIDs;

    private MulticastDispatcher multicastDispatcher;
    private TCPDispatcher tcpDispatcher;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 4) {
            System.out.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port>");
        } else {
            try {
                final Node node = new Node(args);
                System.out.println("Dispatchers have been initialized");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public Node(String[] args) {

        this.nodeID = Integer.parseInt(args[2]);

        this.clusterIPs = new HashMap<Integer, String>();
        this.clusterPorts = new HashMap<Integer, Integer>();
        this.clusterIDs = new ArrayList<Integer>();

        try {
            this.initDispatchers(args);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initDispatchers(String[] args) throws NumberFormatException, IOException {
        this.multicastDispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        final Thread multicastThread = new Thread(this.multicastDispatcher);
        multicastThread.start();

        this.tcpDispatcher = new TCPDispatcher(Integer.parseInt(args[3]), this);
        final Thread tcpThread = new Thread(this.tcpDispatcher);
        tcpThread.start();
    }

    public void handleElection() {
        final String electionMessage = "ELECTION" + " id-" + Integer.toString(this.nodeID);

    }
}
