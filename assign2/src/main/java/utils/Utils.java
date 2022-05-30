package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static byte[] calculateHash(byte[] value) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(value);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error getting hashing algorithm: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
