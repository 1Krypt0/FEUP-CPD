package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static final String CRLF = "\r\n";
    public static int findHeaderEnd(byte[] message) {
        byte[] CRLFBytes = Utils.CRLF.getBytes();
        for (int i = 0; i < message.length - 3; i++) {
            if (message[i] == CRLFBytes[0] && message[i + 1] == CRLFBytes[1]) {
                if (message[i + 2] == CRLFBytes[0] && message[i + 3] == CRLFBytes[1]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static byte[] getHash(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value);
    }
}
