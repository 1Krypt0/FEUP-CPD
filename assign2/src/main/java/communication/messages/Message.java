package communication.messages;

import store.Store;

public abstract class Message {
    public static final String CRLF = "\r\n";

    public abstract void handleMessage();

    public static Message parseMessage(byte[] msg, Store node) {

        String[] messageHeader = separateHeader(msg);
        String messageBody = separateBody(msg);
        String messageType = messageHeader[0];

        switch (messageType) {
        case "JOIN":
            return new JoinMessage(node, messageHeader);
        case "LEAVE":
            return new LeaveMessage(node, messageHeader);
        case "MEMBERSHIP":
            return new MembershipMessage(node, messageHeader, messageBody);
        case "PUT":
            return new PutMessage(messageBody, node);
        case "GET":
            return new GetMessage(messageBody, node);
        case "DELETE":
            return new DeleteMessage(messageBody, node);
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
        String messageBody = message.length == 1 ? "" : message[1].trim();
        return messageBody;
    }
}
