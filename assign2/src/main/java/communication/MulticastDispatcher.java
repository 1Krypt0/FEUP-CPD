package communication;

import java.io.IOException;
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

    @Override
    public void run() {
        while (true) {

        }
    }
}
