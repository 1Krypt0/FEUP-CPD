package communication.messages;

import jdk.jshell.execution.Util;
import store.Store;
import utils.Utils;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class PutMessage extends Message{
        private final byte[] body;

        public PutMessage(byte[] body, Store store) {
            super(store);
            this.body = body;
        }

        @Override
        public void handleMessage() {
            try {
                //TODO: To anything with the data that this function returns (send it to the client)
                store.put(Utils.getHash(this.body), new String(this.body, StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        public static byte[] composeMessage(String fileName) {
            String string = "PUT" +
                    Utils.CRLF + Utils.CRLF +
                    fileName;

            return string.getBytes(StandardCharsets.UTF_8);
        }

}
