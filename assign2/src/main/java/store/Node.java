package store;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import communication.messages.JoinMessage;
import communication.messages.MembershipMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {
    private static final String LOCALHOST = "localhost";

    private final int nodeID;
    private final int tcpPort;
    private int membershipCounter;
    private int receivedMembershipMessages;

    private final LogManager logManager;

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
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public Node(String[] args) {

        this.membershipCounter = -1;
        this.receivedMembershipMessages = 0;
        this.nodeID = Integer.parseInt(args[2]);
        this.tcpPort = Integer.parseInt(args[3]);

        this.clusterIPs = new HashMap<Integer, String>();
        this.clusterPorts = new HashMap<Integer, Integer>();
        this.clusterIDs = new ArrayList<Integer>();

        this.logManager = new LogManager(this.nodeID);

        try {
            this.initDispatchers(args);
            this.enterCluster();
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

        this.tcpDispatcher = new TCPDispatcher(this.tcpPort, this);
        final Thread tcpThread = new Thread(this.tcpDispatcher);
        tcpThread.start();
    }

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

    public void receiveMembershipMessage(int senderID, String members, String body) {
        if (senderID == this.nodeID) {
            System.out.println("This MEMBERSHIP Message came from myself, ignoring");
        } else {
            this.receivedMembershipMessages++;
            System.out.println("Handling membership message");
            this.logManager.writeToLog("This is amazing");
        }
    }

    public void receiveJoinMessage(int senderID, int membershipCounter, String senderIP, int senderPort) {
        if (senderID == nodeID) {
            System.out.println("This message came from me, I will ignore it");
        } else {
            System.out.println("This message came from node " + senderID + " who has membership of " + membershipCounter
                    + " and a port of " + senderPort);
            // TODO: Change to appropriate IP when it is available
            sendMembershipMessage(senderIP, senderPort);
        }
    }

    // NOTE: For now, the destination IP is localhost because we are not sure if the
    // ID will be the same as the ip
    private void sendJoinMessage() {
        final byte[] msg = JoinMessage.composeMessage(this.nodeID, this.membershipCounter, "localhost", this.tcpPort);
        this.multicastDispatcher.sendMessage(msg);
        System.out.println("Sent a join message with contents " + new String(msg));
    }

    private void sendMembershipMessage(String destinationIP, int destinationPort) {
        // TODO: Change to proper data
        final byte[] msg = MembershipMessage.composeMessage(this.nodeID, "members", "Important log Data");
        this.tcpDispatcher.sendMessage(msg, destinationIP, destinationPort);
    }
}
