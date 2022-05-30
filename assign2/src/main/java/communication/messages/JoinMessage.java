package communication.messages;

import store.Node;

public class JoinMessage extends Message {

    private final String[] header;
    private final Node node;

    public JoinMessage(String[] header, Node node) {
        this.header = header;
        this.node = node;
    }

    @Override
    public void handleMessage() {
        int senderID = Integer.parseInt(this.header[1].split(":")[1]);
        int membershipCounter = Integer.parseInt(this.header[2].split(":")[1]);
        int port = Integer.parseInt(this.header[3].split(":")[1]);
        String ip = this.header[4].trim().split(":")[1];
        System.out.println("HANDLING JOIN");
        System.out.println("Sender ID is " + senderID);
        System.out.println("Membershup counter is " + membershipCounter);
        System.out.println("Port is " + port);
        System.out.println("IP is " + ip);
        node.receiveJoinMessage(senderID, membershipCounter, ip, port);
    }

    public static byte[] composeMessage(int id, int membershipCounter, String ip, int port) {
        return ("JOIN id:" + id + " membership:" + membershipCounter + " port:" + port + " ip:" + ip).getBytes();
    }
}
