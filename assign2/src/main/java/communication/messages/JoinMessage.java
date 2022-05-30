package communication.messages;

public class JoinMessage extends Message {

    @Override
    public void handleMessage() {

    }

    public byte[] composeMessage(int id, int membershipCounter) {
        return ("JOIN id:" + id + " membership:" + membershipCounter).getBytes();
    }
}
