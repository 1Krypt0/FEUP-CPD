package store;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageManager implements Serializable {
    private static final String STORAGE_DIR = "/storage/";
    private final String ROOT_DIR;

    public StorageManager(String nodeDir) {
        this.ROOT_DIR = STORAGE_DIR + nodeDir + "/";

        try {
            String absolutePath = System.getProperty("user.dir") + ROOT_DIR;
            Path path = Paths.get(absolutePath);
            Files.createDirectory(path);
            System.out.println("Storage directory has been created");
        } catch (IOException e) {
            System.out.println("Error creating directories" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteFile(String fileName) {
        File file = new File(getStorageDir() + fileName);

        if (file.delete()) {
            System.out.println("Deleted file " + fileName);
        } else {
            System.out.println("Error deleting file " + fileName);
        }
    }

    public String getStorageDir() {
        return System.getProperty("user.dir") + ROOT_DIR;
    }

    public String readFile(String fileName) throws IOException {
        Path filePath = Paths.get(getStorageDir() + fileName);
        return new String(Files.readAllBytes(filePath));
    }

    // TODO: 5/17/22 Implement Storage functions to abstract details of dealing with files
    public String writeFile(String fileName, String fileContents) {
        return "";
    }


}
