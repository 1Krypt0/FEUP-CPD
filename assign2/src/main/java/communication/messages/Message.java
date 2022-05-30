package communication.messages;

import java.util.Arrays;

import store.Store;
import utils.Utils;

public abstract class Message {

    protected final Store store;

    public Message(Store store) {
        this.store = store;
    }

    public Message() {
        this.store = null;
    }

    public abstract void handleMessage();


    public static Message parseMessage(byte[] messageData, Store store) {
        int headerEndIdx = Utils.findHeaderEnd(messageData);
        if (headerEndIdx == -1) {
            return null;
        }
        String[] messageHeaders = new String(Arrays.copyOf(messageData, headerEndIdx + 1)).split(Utils.CRLF);

        byte[] messageBody = Arrays.copyOfRange(messageData, headerEndIdx + 5, messageData.length);

        switch (messageHeaders[0]) {
        case "JOIN":
            return new JoinMessage();
        case "LEAVE":
            return new LeaveMessage();
        case "MEMBERSHIP":
            return new MembershipMessage();
        case "PUT":
            return new PutMessage(messageBody, store);
        case "GET":
            return new GetMessage(messageBody, store);
        case "DELETE":
            return new DeleteMessage(messageBody, store);
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

// TODO: 5/19/22 If the joiner does not get 3 messages, it should retransmit the
// join one to a max of 3 times
