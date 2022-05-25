package communication.messages;

import store.Store;

public class LeaveMessage extends Message {

    public LeaveMessage(Store store, String[] header, String[] body) {
        super(store, header, body);
    }

    @Override
    public void handleMessage() {
    }

    public static byte[] buildMessage() {
        return null;

    }

}
