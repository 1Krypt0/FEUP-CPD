package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

public class PutMessage extends Message {
    private final String body;
    private final Store node;

    public PutMessage(Store node, String body) {
        this.body = body;
        this.node = node;
    }

    @Override
    public void handleMessage() throws RemoteException {
        // TODO: To anything with the data that this function returns (send it to the
        // client)
        // node.put(new String(Utils.calculateHash(this.body.getBytes())), this.body);
        // TODO Implement with proper functionality
        node.put(this.body);

    }

    public static byte[] composeMessage(String fileName) {
        String string = "PUT" + Message.CRLF + Message.CRLF + fileName;

        return string.getBytes(StandardCharsets.UTF_8);
    }

}
