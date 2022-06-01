package communication.messages;

import store.Node;
import utils.Utils;

public abstract class Message {
    public static final String CRLF = "\r\n";

    public abstract void handleMessage();

    public static Message parseMessage(byte[] messageData, Store store) {

        String[] messageHeader = separateHeader(msg);
        String messageBody = separateBody(msg);
        String messageType = messageHeader[0];
        switch (messageHeaders[0]) {
        case "JOIN":
            return new JoinMessage(node, messageHeader);
        case "LEAVE":
            return new LeaveMessage(node, messageHeader);
        case "MEMBERSHIP":
            return new MembershipMessage(node, messageHeader, messageBody);
        case "PUT":
            return new PutMessage(messageBody, store);
        case "GET":
            return new GetMessage(messageBody, store);
        case "DELETE":
            return new DeleteMessage(messageBody, store);
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
