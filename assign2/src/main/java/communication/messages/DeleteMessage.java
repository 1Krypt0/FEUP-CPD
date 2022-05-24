package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;

public class DeleteMessage extends Message {

    private final String body;
    public DeleteMessage(String messageBody, Store store) {
        super(store);
        this.body = messageBody;
    }

    @Override
    public void handleMessage() {
        store.delete(body.getBytes(StandardCharsets.UTF_8));
    }
}
