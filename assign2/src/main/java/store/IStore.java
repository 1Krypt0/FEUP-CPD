package store;

import java.security.NoSuchAlgorithmException;

public interface IStore {
    void put(byte[] key, byte[] value) throws NoSuchAlgorithmException; // TODO: 4/28/22 Change void return to actual
                                                                        // type

    String get(byte[] key);

    void delete(byte[] key);
}
