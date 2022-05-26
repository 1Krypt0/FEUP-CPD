package communication.messages;

import store.Node;

public abstract class Message {
    public abstract void handleMessage();

    public static Message parseMessage(byte[] msg, Node node) {
        return null;
        // TODO: Get message header
        // Switch case with headers
        // Return appropriate child class
    }
}
