package communication.messages;

import store.Store;

public class JoinMessage extends Message {

    public JoinMessage(Store store) {
        super(store);
    }

    @Override
    public void handleMessage() {

    }

}
