package communication.messages;

import store.Node;
import utils.Utils;

public abstract class Message {
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
            break;
        }
        return null;
    }
}
