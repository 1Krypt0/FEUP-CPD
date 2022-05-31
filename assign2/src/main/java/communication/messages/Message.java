package communication.messages;

import store.Node;

public abstract class Message {
    public static final String CRLF = "\r\n";

    public abstract void handleMessage();

    public static Message parseMessage(byte[] msg, Node node) {

        String[] messageHeader = separateHeader(msg);
        String messageBody = separateBody(msg);
        String messageType = messageHeader[0];

        switch (messageType) {
        case "JOIN":
            return new JoinMessage(messageHeader, node);
        case "LEAVE":
            return new LeaveMessage();
        case "MEMBERSHIP":
            return new MembershipMessage(node, messageHeader, messageBody);
        case "ELECTION":
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

    private static String separateBody(byte[] msg) {
        String[] message = new String(msg).split(CRLF + CRLF);
        String messageBody = message.length == 1 ? "" : message[1];
        return messageBody;
    }
}
