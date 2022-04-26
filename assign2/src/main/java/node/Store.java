package node;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Store implements IStore {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] res = new Store().getHash(new byte[] {0x2});
        System.out.println(res.length);
    }

    private byte[] getHash(byte[] value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        return digest.digest(value);
    }

    @Override
    public void put(byte[] key, String value) {
    }

    @Override
    public String get(byte[] key) {
        return null;
    }

    @Override
    public void delete(byte[] key) {

    }
}
