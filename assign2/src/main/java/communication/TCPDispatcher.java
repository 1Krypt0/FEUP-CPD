package communication;

import java.io.IOException;
import java.io.InputStream;
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

    public TCPDispatcher(int port, Store store) throws IOException {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.buf = new byte[512];
        this.store = store;
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
}
