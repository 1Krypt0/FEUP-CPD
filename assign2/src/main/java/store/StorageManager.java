package store;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageManager{
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

    public boolean deleteFile(String fileName) {
        File file = new File(getStorageDir() + fileName);

        if (file.delete()) {
            System.out.println("Deleted file " + fileName);
            return true;
        } else {
            System.out.println("Error deleting file " + fileName);
            return false;
        }
    }

    public String getStorageDir() {
        return System.getProperty("user.dir") + ROOT_DIR;
    }

    public String readFile(String fileName) {
        try {
            Path path = Paths.get(getStorageDir() + fileName);
            byte[] data = Files.readAllBytes(path);
            return new String(data);
        } catch (IOException e) {
            System.out.println("Error reading file " + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public boolean writeFile(String fileName, String fileContents) {
        try {
            Path filePath = Paths.get(getStorageDir() + fileName);
            Files.write(filePath, fileContents.getBytes());
            System.out.println("Successfully wrote file " + fileName);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing file " + fileName + ": " + e.getMessage());
            return false;
        }
    }

}