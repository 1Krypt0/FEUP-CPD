package communication.messages;

import store.Node;

import java.nio.charset.StandardCharsets;

public class DeleteMessage extends Message {

    private final String body;
    private final Node node;

    public DeleteMessage(String messageBody, Node node) {
        this.body = messageBody;
        this.node = node;
    }

    @Override
    public void handleMessage() {
        // TODO: Deal with extracting the data here, only pass actual body
        // Check Membership, Join and Delete for Examples
        node.delete(this.body);
    }

    public static byte[] composeMessage(String data) {
        String string = "DELETE" + Message.CRLF + Message.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
