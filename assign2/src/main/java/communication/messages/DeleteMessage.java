package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;

public class DeleteMessage extends Message {

    private final byte[] body;
    public DeleteMessage(byte[] messageBody, Store store) {
        super(store);
        this.body = messageBody;
    }

    @Override
    public void handleMessage() {
        store.delete(this.body);
    }
}
