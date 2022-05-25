package communication.messages;

import store.Store;
import utils.Utils;

public class MembershipMessage extends Message {

    private final String[] header;
    private final String[] body;

    public MembershipMessage(Store store, String[] header, String[] body) {
        super(store, header, body);
        this.header = header;
        this.body = body;
    }

    @Override
    public void handleMessage() {

    }

    public static byte[] buildMessage(String memberships, String log) {
        return ("MEMBERSHIP" + Utils.CRLF + Utils.CRLF + memberships + " " + log).getBytes();
    }

}
