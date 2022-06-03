package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

public class DeleteMessage extends Message {

    private final String body;
    private final Store node;

    public DeleteMessage(Store node, String messageBody) {
        this.body = messageBody;
        this.node = node;
    }

    @Override
    public void handleMessage() throws RemoteException {
        // TODO: Deal with extracting the data here, only pass actual body
        // Check Membership, Join and Delete for Examples
        node.delete(this.body);
    }

    public static byte[] composeMessage(String data) {
        String string = "DELETE" + Message.CRLF + Message.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
