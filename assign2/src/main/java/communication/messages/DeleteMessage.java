package communication.messages;

import store.Store;
import utils.Utils;

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

    public static byte[] composeMessage(String data) {
        String string = "DELETE" + Utils.CRLF + Utils.CRLF + data;
        return string.getBytes(StandardCharsets.UTF_8);
    }
}
