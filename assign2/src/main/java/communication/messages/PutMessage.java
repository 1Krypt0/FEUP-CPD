package communication.messages;

import store.Node;
import utils.Utils;

import java.nio.charset.StandardCharsets;

public class PutMessage extends Message {
    private final String body;
    private final Node node;

    public PutMessage(String body, Node node) {
        this.body = body;
        this.node = node;
    }

    @Override
    public void handleMessage() {
        // TODO: To anything with the data that this function returns (send it to the
        // client)
        node.put(new String(Utils.calculateHash(this.body.getBytes())), this.body);
    }

    public static byte[] composeMessage(String fileName) {
        String string = "PUT" + Message.CRLF + Message.CRLF + fileName;

        return string.getBytes(StandardCharsets.UTF_8);
    }

}
