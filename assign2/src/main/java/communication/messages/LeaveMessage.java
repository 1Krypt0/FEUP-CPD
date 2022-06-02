package communication.messages;

import store.Store;

public class LeaveMessage extends Message {

    private final Store node;
    private final String[] header;

    public LeaveMessage(Store node, String[] header) {
        this.node = node;
        this.header = header;
    }

    @Override
    public void handleMessage() {
        int senderID = Integer.parseInt(this.header[1].split(":")[1]);
        int membershipCounter = Integer.parseInt(this.header[2].split(":")[1]);
        this.node.receiveLeaveMessage(senderID, membershipCounter);
    }

    public static byte[] composeMessage(int nodeID, int membershipCounter) {
        return ("LEAVE id:" + nodeID + " membership:" + membershipCounter).getBytes();
    }
}
