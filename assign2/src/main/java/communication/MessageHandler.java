package communication;

import java.io.IOException;

import communication.messages.Message;
import store.Store;

public class MessageHandler implements Runnable {

    private final String messageData;
    private final Store store;

    public MessageHandler(String messageData, Store store) throws IOException {
        this.messageData = messageData;
        this.store = store;
    }

    @Override
    public void run() {
        Message message = Message.parseMessage(messageData, store);
        if (message != null) {
            message.handleMessage();
        }
    }

}
