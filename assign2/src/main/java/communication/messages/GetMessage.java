package communication.messages;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

import store.Store;

public class GetMessage extends Message {

    private final String body;
    private final Store node;

    public GetMessage(String messageBody, Store node) {
        this.node = node;
        this.body = messageBody;
    }

    // TODO: Deal with handling the message here
    @Override
    public void handleMessage() throws RemoteException {
        node.get(this.body);
    }

    public static byte[] composeMessage(String data) {
        String string = "GET" + Message.CRLF + Message.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
