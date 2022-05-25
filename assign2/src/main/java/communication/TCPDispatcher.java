package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import store.Store;

public class TCPDispatcher extends Thread {
    private final int port;
    private final ExecutorService executorService;
    private final byte[] buf;
    private final Store store;
    private final ServerSocket serverSocket;

    public TCPDispatcher(int port, Store store) throws IOException {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.buf = new byte[512];
        this.store = store;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket = serverSocket.accept();
                InputStream stream = socket.getInputStream();
                int length = stream.read(buf);
                executorService.submit(new MessageHandler(buf, length, store));
            } catch (IOException e) {
                System.out.println("Error " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(byte[] message) {
        try {
            Socket sender = serverSocket.accept();
            OutputStream stream = sender.getOutputStream();
            stream.write(message);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            System.out.println("Error sending TCP message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
