package store;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import communication.messages.JoinMessage;
import communication.messages.MembershipMessage;
import communication.messages.Message;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
    private final int nodeID;
    private final int tcpPort;
    private int membershipCounter;
    private int receivedMembershipMessages;

    private final LogManager logManager;

    private final HashMap<Integer, String> clusterIPs;
    private final HashMap<Integer, Integer> clusterPorts;
    private List<Integer> clusterIDs;

    private MulticastDispatcher multicastDispatcher;
    private TCPDispatcher tcpDispatcher;

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 4) {
            System.out.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port>");
        } else {
            try {
                final Node node = new Node(args);
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

        this.clusterIDs.add(this.nodeID);

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
        } else {
            this.receivedMembershipMessages++;
            List<Integer> clusterMembers = Arrays.stream(members.split("-")).map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());
            clusterIDs = Utils.getListUnion(clusterIDs, clusterMembers);
            System.out.println("The updated cluster members are " + clusterIDs.toString());
            System.out.println("The body is " + body);
            this.logManager.writeToLog(body);
        }
    }

    public void receiveJoinMessage(int senderID, int membershipCounter, String senderIP, int senderPort) {
        if (senderID == nodeID) {
        } else {
            if (!clusterIDs.contains(senderID)) {
                // Update internal cluster state
                this.clusterIDs.add(senderID);
                this.clusterIPs.put(senderID, senderIP);
                this.clusterPorts.put(senderID, senderPort);

                // Add to log events
                String logMessage = new Date().toString() + " " + Integer.toString(senderID) + " JOIN "
                        + Integer.toString(membershipCounter);
                this.logManager.writeToLog(logMessage);
                sendMembershipMessage(senderIP, senderPort);
            }
        }
    }

    // NOTE: For now, the destination IP is localhost because we are not sure if the
    // ID will be the same as the ip
    private void sendJoinMessage() {
        final byte[] msg = JoinMessage.composeMessage(this.nodeID, this.membershipCounter, "localhost", this.tcpPort);
        this.multicastDispatcher.sendMessage(msg);
        System.out.println("Sent a JOIN message with contents " + new String(msg));
    }

    private void sendMembershipMessage(String destinationIP, int destinationPort) {
        final List<String> recent32LogEvents = logManager.get32MostRecentLogMessages();
        String logEvents = "";
        String clusterMembers = "";

        for (String event : recent32LogEvents) {
            logEvents += event + "\n";
        }

        for (int id : clusterIDs) {
            clusterMembers += Integer.toString(id) + "-";
        }

        clusterMembers = clusterMembers.substring(0, clusterMembers.length() - 1);

        final byte[] msg = MembershipMessage.composeMessage(this.nodeID, clusterMembers, logEvents);
        System.out.println("Sent a MEMBERSHIP message with contents " + new String(msg));
        this.tcpDispatcher.sendMessage(msg, destinationIP, destinationPort);
    }
}
