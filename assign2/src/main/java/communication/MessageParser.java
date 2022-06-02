package communication;

import communication.messages.Message;
import store.Node;

/**
 * Class that will be run inside the TCP and {@link MulticastDispatcher} Will
 * parse the message and handle with the appropriate one
 */
public class MessageParser implements Runnable {

    private final byte[] msg;
    private final Node node;

    public MessageParser(byte[] msg, Node node) {
        this.msg = msg;
        this.node = node;
    }

    @Override
    public void run() {
        Message message = Message.parseMessage(msg, node);
        message.handleMessage();
    }
}
