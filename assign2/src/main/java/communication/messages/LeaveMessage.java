package communication.messages;

import store.Store;

public class LeaveMessage extends Message {

    public LeaveMessage(Store store) {
        super(store);
    }

    @Override
    public void handleMessage() {
    }

}
