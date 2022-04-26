package node;

public interface IStore {
    void put(byte[] key, String value);

    String get(byte[] key);

    void delete(byte[] key);
}
