import java.security.MessageDigest;

public class Utils {
    public static byte[] calculateHash(byte[] value) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(value);
    }
}
