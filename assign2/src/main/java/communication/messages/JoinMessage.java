package communication.messages;

import store.Store;
import utils.Utils;

import java.nio.charset.StandardCharsets;

public class JoinMessage extends Message {

    public JoinMessage() {
        super();
    }

    @Override
    public void handleMessage() {
    }

    public static byte[] composeMessage(int nodeId, int membershipCounter) {
        String string = "JOIN" + Utils.CRLF + Utils.CRLF + nodeId + Utils.CRLF + membershipCounter;
        return string.getBytes(StandardCharsets.UTF_8);
    }

}
