package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

public class DeleteMessage extends Message {

    private final String body;
    private final Store node;
    private final String[] header;

    public DeleteMessage(Store node, String[] messageHeader, String messageBody) {
        this.body = messageBody;
        this.node = node;
        this.header = messageHeader;
    }

    @Override
    public void handleMessage() throws RemoteException {
        // TODO: Deal with extracting the data here, only pass actual body
        // Check Membership, Join and Delete for Examples
        node.delete(this.body);
    }

    public static byte[] composeMessage(String data, String ip, int port) {
        String string = "DELETE" + " ip:" + ip + " port:" + Integer.toString(port) + Message.CRLF + Message.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
