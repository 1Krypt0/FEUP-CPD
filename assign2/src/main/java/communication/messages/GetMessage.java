package communication.messages;

import store.Store;
import utils.Utils;

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

    public static byte[] composeMessage(String data) {
        String string = "GET" + Utils.CRLF + Utils.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
