package communication.messages;

import store.Store;

import java.nio.charset.StandardCharsets;

public class GetMessage extends Message {

    private final byte[] body;
    public GetMessage(byte[] messageBody, Store store) {
        super(store);
        this.body = messageBody;
    }

    @Override
    public void handleMessage() {
        store.get(this.body);
    }
}
