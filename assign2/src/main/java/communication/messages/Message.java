package communication.messages;

import store.Node;

public abstract class Message {
    private static final String CRLF = "\r\n";

    public abstract void handleMessage();

    public static Message parseMessage(byte[] msg, Node node) {

        System.out.println("Message is " + msg.toString());

        String messageType = separateHeader(msg)[0];

        switch (messageType) {
        case "JOIN":
            System.out.println("This is a JOIN message");
            return new JoinMessage();
        case "LEAVE":
            System.out.println("This is a LEAVE message");
            return new LeaveMessage();
        case "MEMBERSHIP":
            System.out.println("This is a MEMBERSHIP message");
            return new MembershipMessage();
        default:
            break;
        }
        return null;
    }

    private static String[] separateHeader(byte[] msg) {
        String[] message = new String(msg).split(CRLF + CRLF);
        String[] messageHeader = message[0].split(CRLF);
        return messageHeader;
    }
}
