package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;

public class GetMessage extends Message {

    private final String body;
    public GetMessage(String messageBody, Store store) {
        super(store);
        this.body = messageBody;
    }

    @Override
    public void handleMessage() {
        store.get(body.getBytes(StandardCharsets.UTF_8));
    }
}
