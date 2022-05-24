package communication.messages;

import store.Store;
import utils.Utils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class PutMessage extends Message{
        private final String body;

        public PutMessage(String body, Store store) {
            super(store);
            this.body = body;
        }

        @Override
        public void handleMessage() {
            try {
                //TODO: To anything with the data that this function returns (send it to the client)
                store.put(Utils.getHash(this.body.getBytes(StandardCharsets.UTF_8)), this.body);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

}
