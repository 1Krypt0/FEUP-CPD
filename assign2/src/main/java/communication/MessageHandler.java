package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import communication.messages.Message;
import store.Store;

public class MessageHandler implements Runnable {

    private final byte[] messageData;
    private final int messsageLength;
    private final Store store;

    public MessageHandler(byte[] messageData, int messageLength, Store store) throws IOException {
        this.messageData = messageData;
        this.messsageLength = messageLength;
        this.store = store;
    }

    @Override
    public void run() {
        Message message = Message.parseMessage(messageData, messsageLength, store);
        if (message != null) {
            message.handleMessage();
        }
    }

}
