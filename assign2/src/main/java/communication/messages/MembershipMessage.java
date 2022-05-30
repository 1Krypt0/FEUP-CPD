package communication.messages;

import store.Node;

public class MembershipMessage extends Message {

    private final Node node;

    public MembershipMessage(Node node) {
        this.node = node;
    }

    @Override
    public void handleMessage() {
        this.node.receiveMembershipMessage();
    }

    public byte[] composeMessage() {
        return null;
    }

}
