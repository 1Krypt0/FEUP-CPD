package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import store.Node;

public class TCPDispatcher extends Thread {

    private final ExecutorService executorService;
    private final Node node;
    private final ServerSocket serverSocket; // Passive socket for listening to messages

    public TCPDispatcher(final int port, final Node node) throws IOException {
        this.executorService = Executors.newCachedThreadPool();
        this.node = node;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Socket socket = serverSocket.accept();

                final InputStream stream = socket.getInputStream();

                final byte[] msg = stream.readAllBytes();

                executorService.submit(new MessageParser(msg, node));

            } catch (final IOException e) {
                System.out.println("Error reading TCP message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(final byte[] msg, final String destinationIP, final int destinationPort) {
        try {
            final InetAddress address = InetAddress.getByName(destinationIP);
            final Socket socket = new Socket(address, destinationPort);

            final OutputStream stream = socket.getOutputStream();
            final PrintWriter writer = new PrintWriter(stream, true);
            final String message = new String(msg);

            writer.println(message);

            socket.close();

        } catch (final UnknownHostException e) {
            System.out.println("Could not find remote machine");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error sending TCP message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
