package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import store.Node;

public class MulticastDispatcher extends Thread {

    private final ExecutorService executorService;
    private final Node node;
    private final MulticastSocket socket;
    private final byte[] buf;

    public MulticastDispatcher(String ip, int port, Node node) throws IOException {
        this.executorService = Executors.newCachedThreadPool();
        this.node = node;
        this.buf = new byte[512];
        this.socket = new MulticastSocket(port);
        SocketAddress address = new InetSocketAddress(ip, port);
        NetworkInterface networkInterface = NetworkInterface.getByName(ip);
        this.socket.joinGroup(address, networkInterface);
    }

    /**
     * Keep loop tight to be highly available. Use threads to handle everything
     */
    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                System.out.println("Multicast Dispatcher Received a Message!");
                executorService.submit(new MessageParser(packet.getData(), node));
            } catch (IOException e) {
                System.out.println("Error receiving multicast packet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(byte[] msg) {
        DatagramPacket packet = new DatagramPacket(msg, msg.length);

        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error sending Multicast Messages: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
