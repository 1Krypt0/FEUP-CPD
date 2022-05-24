package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import store.Store;

public class MulticastDispatcher extends Thread {
    private final String ip;
    private final int port;
    private final ExecutorService executorService;
    private final MulticastSocket socket;
    private final InetAddress group;
    private final byte[] buf;
    private final Store store;

    public MulticastDispatcher(String ip, int port, Store store) throws IOException {
        this.ip = ip;
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
        this.socket = new MulticastSocket(port);
        this.group = InetAddress.getByName(ip);
        this.socket.joinGroup(group);
        this.buf = new byte[256];
        this.store = store;
    }

    /**
     *
     * Loop responsible for receiving Messages from other nodes in the network
     * Executor service launches a thread that will handle the message accordingly
     * That message will interface with the store to do the right actions
     */
    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                executorService.submit(new MessageHandler(new String(packet.getData()), store));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error receiving message: " + e.getMessage());
            }
        }

    }

    public void sendMessage(byte[] message) {
        DatagramPacket packet = new DatagramPacket(message, message.length);

        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending multicast packet: " + e.getMessage());
        }
    }
}
