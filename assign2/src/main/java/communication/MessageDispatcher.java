package communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import communication.messages.Message;

public class MessageDispatcher implements Runnable {

    private final ExecutorService dispatcher;
    private final int port;
    private final ServerSocket serverSocket;

    public MessageDispatcher(int port) throws IOException {
        this.dispatcher = Executors.newCachedThreadPool();
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Dispatcher is listening on port " + this.port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                Message message = Message.parseMessage();

                if (message != null) {
                    message.handleMessage();
                }

            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
