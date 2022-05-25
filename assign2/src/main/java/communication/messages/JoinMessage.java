package communication.messages;

import store.Store;

public class JoinMessage extends Message {

    private final String[] header;
    private final String[] body;

    public JoinMessage(Store store, String[] header, String[] body) {
        super(store, header, body);
        this.header = header;
        this.body = body;
    }

    @Override
    public void handleMessage() {
        try {
            store.addNewNode(header);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static byte[] buildMessage() {
        return null;

    }

}
