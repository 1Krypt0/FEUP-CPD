package communication.messages;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

import store.Store;

public class GetMessage extends Message {

    private final String body;
    private final Store node;
    private final String[] header;

    public GetMessage(Store node, String[] messageHeader, String messageBody) {
        this.node = node;
        this.body = messageBody;
        this.header = messageHeader;
    }

    // TODO: Deal with handling the message here
    @Override
    public void handleMessage() throws RemoteException {
        node.get(this.body);
    }

    public static byte[] composeMessage(String data, String ip, int port) {
        String string = "GET" + " ip:" + ip + " port:" + Integer.toString(port) + Message.CRLF + Message.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
