package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
    private final SocketAddress address;
    private final int port;
    private final String ip;

    public MulticastDispatcher(final String ip, final int port, final Node node) throws IOException {
        this.executorService = Executors.newCachedThreadPool();
        this.node = node;
        this.buf = new byte[512];
        this.socket = new MulticastSocket(port);
        this.address = new InetSocketAddress(ip, port);
        this.ip = ip;
        this.port = port;
        final NetworkInterface networkInterface = NetworkInterface.getByName(ip);
        this.socket.joinGroup(address, networkInterface);
    }

    /**
     * Keep loop tight to be highly available. Use threads to handle everything
     */
    @Override
    public void run() {
        while (true) {
            final DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                executorService.submit(new MessageParser(packet.getData(), node));
            } catch (final IOException e) {
                System.out.println("Error receiving multicast packet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(final byte[] msg) {
        InetAddress address;
        try {
            address = InetAddress.getByName(this.ip);
            final DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);

            socket.send(packet);
        } catch (final IOException e) {
            System.out.println("Error sending Multicast Messages: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
