package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static byte[] getHash(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value);
    }
}
