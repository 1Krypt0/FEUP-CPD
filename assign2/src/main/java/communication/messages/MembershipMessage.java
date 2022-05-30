package communication.messages;

import store.Node;

public class MembershipMessage extends Message {

    private final Node node;
    private final String[] messageHeader;
    private final String messageBody;

    public MembershipMessage(Node node, String[] messageHeader, String messageBody) {
        this.node = node;
        this.messageHeader = messageHeader;
        this.messageBody = messageBody;
    }

    @Override
    public void handleMessage() {
        this.node.receiveMembershipMessage(messageHeader, messageBody);
    }

    public byte[] composeMessage() {
        return null;
    }

}
