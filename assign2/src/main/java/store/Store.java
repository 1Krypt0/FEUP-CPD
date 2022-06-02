package store;

import communication.MulticastDispatcher;
import communication.PeriodicMulticastMessageSender;
import communication.RMI;
import communication.TCPDispatcher;
import communication.messages.JoinMessage;
import communication.messages.LeaveMessage;
import communication.messages.MembershipMessage;
import utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Store implements RMI {
    private final String nodeID;
    private final int tcpPort;
    private final MembershipCounterManager membershipCounterManager;
    private int receivedMembershipMessages;

    private final LogManager logManager;

    private final HashMap<String, String> clusterIPs;
    private final HashMap<String, Integer> clusterPorts;
    private List<String> clusterIDs;

    private MulticastDispatcher multicastDispatcher;
    private TCPDispatcher tcpDispatcher;
    private PeriodicMulticastMessageSender periodicSender;

    public static void main(final String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port>");
        } else {
            try {
                final Store node = new Store(args);

                node.initDispatchers(args);

                RMI stub = (RMI) UnicastRemoteObject.exportObject(node, 0);

                Registry registry = LocateRegistry.getRegistry();
                registry.bind(args[2], stub);

                System.out.println("The server is ready");

            } catch (final IOException e) {
                System.out.println("Node communication error: " + e.getMessage());
                e.printStackTrace();
            } catch (AlreadyBoundException e1) {
                System.out.println("Error binding ID: " + e1.getMessage());
                e1.printStackTrace();
            }
        }

    }

    public List<String> getClusterIDs() {
        return this.clusterIDs;
    }

    public String getID() {
        return this.nodeID;
    }

    public Store(final String[] args) {

        this.receivedMembershipMessages = 0;
        this.nodeID = args[2];
        this.tcpPort = Integer.parseInt(args[3]);

        this.membershipCounterManager = new MembershipCounterManager(this.nodeID);

        this.clusterIPs = new HashMap<String, String>();
        this.clusterPorts = new HashMap<String, Integer>();
        this.clusterIDs = new ArrayList<String>();

        this.clusterIDs.add(this.nodeID);

        this.logManager = new LogManager(this.nodeID);

    }

    public void initDispatchers(final String[] args) throws IOException {
        this.multicastDispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        final Thread multicastThread = new Thread(this.multicastDispatcher);
        multicastThread.start();

        this.tcpDispatcher = new TCPDispatcher(this.tcpPort, this);
        final Thread tcpThread = new Thread(this.tcpDispatcher);
        tcpThread.start();

        this.periodicSender = new PeriodicMulticastMessageSender(this);
        this.periodicSender.updateMembershipPeriodically();
    }

    public int enterCluster() throws UnknownHostException {

        int counterValue = this.membershipCounterManager.getMembershipCounter();
        if (counterValue % 2 == 0) {
            System.out.println("Node has already joined the cluster");
            return -1;
        }

        this.membershipCounterManager.incrementMembershipCounter();
        int sentJoinMessages = 0;
        final String logMessage = this.nodeID + " JOIN "
                + Integer.toString(membershipCounterManager.getMembershipCounter()) + "\n";
        logManager.writeToLog(logMessage);
        while (sentJoinMessages != 3) {
            sendJoinMessage();
            sentJoinMessages++;
            int timeElapsed = 0;
            final long start = System.currentTimeMillis();
            // Waiting for handleMembership to receive messages
            while (this.receivedMembershipMessages < 3 && timeElapsed < 3) {
                final long timeAfter = System.currentTimeMillis();
                timeElapsed = (int) ((timeAfter - start) / 1000);
            }
        }

        return 0;
    }

    public int leaveCluster() {

        int counterValue = this.membershipCounterManager.getMembershipCounter();

        if (counterValue % 2 == 1) {
            System.out.println("Node has already left the cluster");
            return -1;
        }

        this.membershipCounterManager.incrementMembershipCounter();
        final String logMessage = this.nodeID + " LEAVE "
                + Integer.toString(membershipCounterManager.getMembershipCounter()) + "\n";
        logManager.writeToLog(logMessage);
        sendLeaveMessage();
        this.periodicSender.stopLoop();
        this.tcpDispatcher.stopLoop();
        this.multicastDispatcher.stopLoop();

        return 0;
    }

    public void receiveMembershipMessage(final String senderID, final String members, final String body) {
        if (senderID.equals(this.nodeID)) {
        } else {
            this.receivedMembershipMessages++;
            final List<String> clusterMembers = Arrays.asList(members.split("-"));
            clusterIDs = Utils.getListUnion(clusterIDs, clusterMembers);
            System.out.println("The updated cluster members are " + clusterIDs.toString());
            this.logManager.writeToLog(body);
        }
    }

    public void receiveJoinMessage(final String senderID, final int membershipCounter, final String senderIP,
            final int senderPort) {
        if (senderID.equals(nodeID)) {
        } else {
            if (!clusterIDs.contains(senderID)) {
                // Update internal cluster state
                this.clusterIDs.add(senderID);
                this.clusterIPs.put(senderID, senderIP);
                this.clusterPorts.put(senderID, senderPort);

                // Add to log events
                final String logMessage = senderID + " JOIN " + Integer.toString(membershipCounter) + "\n";
                this.logManager.writeToLog(logMessage);
                sendMembershipMessage(senderIP, senderPort);
                Collections.sort(this.clusterIDs);
            }
        }
    }

    public void receiveLeaveMessage(String senderID, int membershipCounter) {
        final String logMessage = senderID + " LEAVE " + Integer.toString(membershipCounter) + "\n";
        this.logManager.writeToLog(logMessage);
        this.clusterIDs.remove(senderID);
        this.clusterIPs.remove(senderID);
        this.clusterPorts.remove(senderID);
    }

    // NOTE: For now, the destination IP is localhost because we are not sure if the
    // ID will be the same as the ip
    private void sendJoinMessage() throws UnknownHostException {
        final byte[] msg = JoinMessage.composeMessage(this.nodeID, membershipCounterManager.getMembershipCounter(),
                InetAddress.getLocalHost().getHostAddress(), this.tcpPort);
        this.multicastDispatcher.sendMessage(msg);
        System.out.println("Sent a JOIN message with contents " + new String(msg));
    }

    private void sendLeaveMessage() {
        final byte[] msg = LeaveMessage.composeMessage(this.nodeID, membershipCounterManager.getMembershipCounter());
        this.multicastDispatcher.sendMessage(msg);
        System.out.println("Sent a LEAVE message with contents " + new String(msg));
    }

    private void sendMembershipMessage(final String destinationIP, final int destinationPort) {
        final List<String> recent32LogEvents = logManager.get32MostRecentLogMessages();
        String logEvents = "";
        String clusterMembers = "";

        for (final String event : recent32LogEvents) {
            logEvents += event + "\n";
        }

        for (final String id : clusterIDs) {
            clusterMembers += id + "-";
        }

        logEvents = logEvents.substring(0, logEvents.length() - 1);
        clusterMembers = clusterMembers.substring(0, clusterMembers.length() - 1);

        final byte[] msg = MembershipMessage.composeMessage(this.nodeID, clusterMembers, logEvents);
        System.out.println("Sent a MEMBERSHIP message with contents " + new String(msg));
        this.tcpDispatcher.sendMessage(msg, destinationIP, destinationPort);
    }

    public void sendMulticastMembership() {
        final List<String> recent32LogEvents = logManager.get32MostRecentLogMessages();
        String logEvents = "";
        String clusterMembers = "";

        for (final String event : recent32LogEvents) {
            logEvents += event + "\n";
        }

        for (final String id : clusterIDs) {
            clusterMembers += id + "-";
        }

        logEvents = logEvents.length() == 0 ? "" : logEvents.substring(0, logEvents.length() - 1);
        clusterMembers = clusterMembers.substring(0, clusterMembers.length() - 1);

        final byte[] msg = MembershipMessage.composeMessage(this.nodeID, clusterMembers, logEvents);
        this.multicastDispatcher.sendMessage(msg);
        System.out.println("Sent a MEMBERSHIP MULTICAST message with contents " + new String(msg));
    }

    @Override
    public String join() throws RemoteException {
        try {
            int res = this.enterCluster();
            if (res == 0) {
                return "Node " + this.nodeID + " has joined the cluster";
            } else {
                return "Node has already joined the cluster";
            }
        } catch (UnknownHostException e) {
            System.out.println("Cannot determine localhost");
            e.printStackTrace();
            return "Error: Cannot determine localhost";
        }
    }

    @Override
    public String leave() throws RemoteException {
        int res = this.leaveCluster();
        if (res == 0) {
            return "Node " + this.nodeID + " has left the cluster";
        } else {
            return "Node has already left the cluster";
        }
    }

    // TODO: Pass actual data, let handlers separate the fields approprietly
    public void put(String fileName) {
        return null;
    }

    public void get(String key) {
        return null;
    }

    public void delete(String key){
        return null;
    }
}
