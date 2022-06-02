package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static List<Integer> getListUnion(List<Integer> l1, List<Integer> l2) {
        Set<Integer> union = new HashSet<>();
        union.addAll(l1);
        union.addAll(l2);
        return new ArrayList<Integer>(union);
    }
}
