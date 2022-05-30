package store;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import communication.messages.JoinMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    private static final String LOCALHOST = "localhost";

    private final int nodeID;
    private int membershipCounter;
    private int receivedMembershipMessages;

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
                node.enterCluster();
                System.out.println("Dispatchers have been initialized");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public Node(String[] args) {

        this.membershipCounter = -1;
        this.receivedMembershipMessages = 0;
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

    // JOIN FLOW
    // Send join message
    // Await for 3 membership messages
    // If after 3 seconds he doesnt receive 3, retransmit join
    // When it receives 3 messages, update log
    // If it doesn't receive 3, stop and don't update log
    //
    public void enterCluster() {
        this.membershipCounter++;
        int sentJoinMessages = 0;

        while (sentJoinMessages != 3) {
            sendJoinMessage();
            sentJoinMessages++;
            int timeElapsed = 0;
            long start = System.currentTimeMillis();
            // Waiting for handleMembership to receive messages
            while (this.receivedMembershipMessages < 3 && timeElapsed < 3) {
                long timeAfter = System.currentTimeMillis();
                timeElapsed = (int) ((timeAfter - start) / 1000);
            }
        }
    }

    public void handleMembership() {
        this.receivedMembershipMessages++;
        System.out.println("Handling membership message");
        // NOTE: Membership handling will go here
    }

    private void sendJoinMessage() {
        // FORMAT
        // JOIN id:<id> membership:<membership>
        JoinMessage message = new JoinMessage();
        byte[] msg = message.composeMessage(this.nodeID, this.membershipCounter);
        this.multicastDispatcher.sendMessage(msg);
    }
}
