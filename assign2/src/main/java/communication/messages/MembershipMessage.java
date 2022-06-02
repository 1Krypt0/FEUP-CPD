package communication.messages;

import store.Node;

public class MembershipMessage extends Message {

    private final Node node;
    private final String[] header;
    private final String body;

    public MembershipMessage(Node node, String[] header, String body) {
        this.node = node;
        this.header = header;
        this.body = body;
    }

    @Override
    public void handleMessage() {
        int senderID = Integer.parseInt(this.header[1].split(":")[1]);
        String members = this.header[2].split(":")[1];
        this.node.receiveMembershipMessage(senderID, members, body);
    }

    public static byte[] composeMessage(int id, String members, String logData) {
        return ("MEMBERSHIP id:" + id + " members:" + members + Message.CRLF + Message.CRLF + logData).getBytes();
    }

}
