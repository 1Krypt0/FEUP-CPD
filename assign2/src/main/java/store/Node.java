package store;

import communication.MulticastDispatcher;
import communication.TCPDispatcher;
import java.io.IOException;
import java.util.Arrays;

public class Node {

    private final MulticastDispatcher multicastDispatcher;
    private final TCPDispatcher tcpDispatcher;

    public static void main(final String[] args) throws InterruptedException {
        if (args.length != 4) {
            System.out.println("Usage: java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port>");
        } else {
            try {
                final Node node = new Node(args);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error creating node: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Node(final String[] args) throws NumberFormatException, IOException, InterruptedException {
        this.multicastDispatcher = new MulticastDispatcher(args[0], Integer.parseInt(args[1]), this);
        final Thread multicastThread = new Thread(this.multicastDispatcher);
        multicastThread.start();

        this.tcpDispatcher = new TCPDispatcher(Integer.parseInt(args[3]), this);
        final Thread tcpThread = new Thread(this.tcpDispatcher);
        tcpThread.start();

        System.out.println("Dispatchers have been initialized");

        System.out.println(Arrays.toString(args));

        if (args[3].equals("8082")) {
            String msg = "This is a really awesome test message";

            this.multicastDispatcher.sendMessage(msg.getBytes());
        }
    }

}
