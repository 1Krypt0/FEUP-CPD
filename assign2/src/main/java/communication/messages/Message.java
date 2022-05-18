package communication.messages;

public abstract class Message {

    public abstract void handleMessage();

    // TODO: Only a stub, change for appropraite factory
    public static Message parseMessage() {
        return new Message() {

            @Override
            public void handleMessage() {
                // TODO Auto-generated method stub

            }

        };
    }
}
