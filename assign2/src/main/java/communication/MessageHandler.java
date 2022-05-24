package communication;

import java.io.IOException;

import communication.messages.Message;
import store.Store;

public class MessageHandler implements Runnable {

    private final byte[] messageData;
    private final int messageLength;
    private final Store store;

    public MessageHandler(byte[] messageData, int messageLength, Store store) throws IOException {
        this.messageData = messageData;
        this.messageLength = messageLength;
        this.store = store;
    }

    @Override
    public void run() {
        Message message = Message.parseMessage(messageData, messageLength, store);
        if (message != null) {
            message.handleMessage();
        }
    }

}
