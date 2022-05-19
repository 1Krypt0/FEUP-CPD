package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import store.Store;
import utils.Utils;

public class MulticastDispatcher extends Thread {
    private final String ip;
    private final int port;
    private final ScheduledExecutorService executorService;
    private final MulticastSocket socket;
    private final InetAddress group;
    private final byte[] buf;
    private final Store store;

    public MulticastDispatcher(String ip, int port, Store store) throws IOException {
        this.ip = ip;
        this.port = port;
        this.executorService = Executors.newScheduledThreadPool(Utils.THREAD_NUMBER);
        this.socket = new MulticastSocket(port);
        this.group = InetAddress.getByName(ip);
        this.socket.joinGroup(group);
        this.buf = new byte[256];
        this.store = store;
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                executorService.submit(new MessageHandler(packet.getData(), packet.getLength(), store));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error receiving message: " + e.getMessage());
            }
        }

    }
}
