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

    public static List<String> getListUnion(List<String> clusterIDs, List<String> clusterMembers) {
        Set<String> union = new HashSet<>();
        union.addAll(clusterIDs);
        union.addAll(clusterMembers);
        return new ArrayList<String>(union);
    }
}
