package communication.messages;

import java.util.Arrays;

import store.Store;
import utils.Utils;

public abstract class Message {

    protected final Store store;
    protected final String[] header;
    protected final String[] body;

    public Message(Store store, String[] header, String[] body) {
        this.store = store;
        this.header = header;
        this.body = body;
    }

    public abstract void handleMessage();

    public static Message parseMessage(byte[] messageData, int messageLength, Store store) {
        int headerEndIdx = Utils.findHeaderEnd(messageData);
        String[] messageHeader = new String(Arrays.copyOf(messageData, headerEndIdx)).split(" ");
        String[] messageBody = new String(Arrays.copyOfRange(messageData, headerEndIdx + 1, messageData.length))
                .split(" ");

        System.out.println("Message header is " + messageHeader);
        System.out.println("Message body is " + messageBody);

        switch (messageHeader[0]) {
        case "JOIN":
            return new JoinMessage(store, messageHeader, messageBody);
        case "LEAVE":
            return new LeaveMessage(store, messageHeader, messageBody);
        case "MEMBERSHIP":
            return new MembershipMessage(store, messageHeader, messageBody);
        default:
            return null;
        }
    }
}

// TODO: 5/19/22 Use MulticastSocket to manage multicast messages. Message must
// have value of membership counter, initially set to 0. Membership counters
// should be stored in a file. When a node receives a message, it should add an
// entry to a log file, with the joining node's id and the counter value

// TODO: 5/19/22 When a node joins the channel, it will receive from other nodes
// a MEMBERSHIP message. This message includes the other nodes in the cluster,
// as well as the 32 most recent log events

// TODO: 5/19/22 Before a node sends a join, it should start by accepting TCP
// connections in this (use a Socket for
// that). The port number should be sent in the Join Message. The receivers of
// the Join message wait a random time
// length and then sends the information. After 3 messages received, it stops
// accepting connections
//
// JOIN message should have as HEADER: JOIN NodeID MembershipCounter PORT
// MEMBERSHIP message - HEADER: MEMBERSHIP
// BODY: LIST OF CURRENT MEMBERS - 32 events in log

// TODO: 5/19/22 If the joiner does not get 3 messages, it should retransmit the
// join one to a max of 3 times
