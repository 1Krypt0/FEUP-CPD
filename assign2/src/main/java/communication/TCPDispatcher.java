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

    public TCPDispatcher(int port, Node node) throws IOException {
        this.executorService = Executors.newCachedThreadPool();
        this.node = node;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                InputStream stream = socket.getInputStream();

                byte[] msg = stream.readAllBytes();

                System.out.println("Received TCP message with contents: " + msg.toString());

                executorService.submit(new MessageParser(msg, node));

            } catch (IOException e) {
                System.out.println("Error reading TCP message: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(byte[] msg, String destinationIP, int destinationPort) throws IOException {

        try {
            InetAddress address = InetAddress.getByName(destinationIP);
            Socket socket = new Socket(address, destinationPort);

            OutputStream stream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(stream, true);
            writer.print(msg);

            socket.close();

        } catch (UnknownHostException e) {
            System.out.println("Could not find remote machine");
            e.printStackTrace();
        }
    }
}
