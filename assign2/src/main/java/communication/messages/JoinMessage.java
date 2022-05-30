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
        int membershipCounter = Integer.parseInt(this.header[2].trim().split(":")[1]);
        System.out.println("HANDLING JOIN");
        System.out.println("Sender ID is " + senderID);
        System.out.println("Membershup counter is " + membershipCounter);
        node.receiveJoinMessage(senderID, membershipCounter);
    }

    public static byte[] composeMessage(int id, int membershipCounter) {
        return ("JOIN id:" + id + " membership:" + membershipCounter).getBytes();
    }
}
