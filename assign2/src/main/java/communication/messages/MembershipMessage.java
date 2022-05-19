package communication.messages;

import store.Store;

public class MembershipMessage extends Message {

    public MembershipMessage(Store store) {
        super(store);
    }

    @Override
    public void handleMessage() {

    }

}
