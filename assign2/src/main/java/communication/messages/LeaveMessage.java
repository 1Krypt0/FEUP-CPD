package communication.messages;

import store.Store;

public class LeaveMessage extends Message {

    public LeaveMessage() {
        super();
    }

    @Override
    public void handleMessage() {
    }

    public static byte[] composeMessage() {
        return null;
    }
}
