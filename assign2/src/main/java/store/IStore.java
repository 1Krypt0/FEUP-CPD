package store;

public interface IStore {
    void put(byte[] key, String value); // TODO: 4/28/22 Change void return to actual type

    String get(byte[] key);

    void delete(byte[] key);
}
