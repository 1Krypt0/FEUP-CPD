package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import communication.messages.Message;

public class MessageHandler implements Runnable {

    private final byte[] messageData;
    private final int messsageLength;

    public MessageHandler(byte[] messageData, int messageLength) throws IOException {
        this.messageData = messageData;
        this.messsageLength = messageLength;
    }

    @Override
    public void run() {
        Message message = Message.parseMessage(messageData, messsageLength);
        if (message != null) {
            message.handleMessage();
        }
    }

}
