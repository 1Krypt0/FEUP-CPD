package communication.messages;

import store.Node;

public abstract class Message {
    private static final String CRLF = "\r\n";

    public abstract void handleMessage();

    public static Message parseMessage(byte[] msg, Node node) {

        String[] messageHeader = separateHeader(msg);
        String messageType = messageHeader[0];

        System.out.println("The message is of type " + messageType);

        switch (messageType) {
        case "JOIN":
            System.out.println("This is a JOIN message");
            return new JoinMessage(messageHeader, node);
        case "LEAVE":
            System.out.println("This is a LEAVE message");
            return new LeaveMessage();
        case "MEMBERSHIP":
            System.out.println("This is a MEMBERSHIP message");
            return new MembershipMessage(node);
        case "ELECTION":
            System.out.println("This is an ELECTION message");
            return new ElectionMessage();
        default:
            break;
        }
        return null;
    }

    private static String[] separateHeader(byte[] msg) {
        String[] message = new String(msg).split(CRLF + CRLF);
        String[] messageHeader = message[0].split(" ");
        return messageHeader;
    }
}
