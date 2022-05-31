package store;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import communication.messages.Message;

public class LogManager {

    private static final String logsDir = "/../logs/";
    private final String logFile;

    public LogManager(int nodeID) {
        String localDir = System.getProperty("user.dir");
        String logDir = localDir + logsDir + nodeID;

        Path logPath = Paths.get(logDir);
        try {
            Files.createDirectories(logPath);
        } catch (IOException e) {
            System.out.println("Error creating log directory: " + e.getMessage());
            e.printStackTrace();
        }

        this.logFile = localDir + logsDir + nodeID + "/log.txt";
        File file = new File(this.logFile);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeToLog(String logMessage) {
        // TODO: Check for repeated messages. Add method to check if message already
        // exists
        List<String> recentLogEntries = get32MostRecentLogMessages();
        String[] individualLogMessages = logMessage.split("\n");
        for (String individualMessage : individualLogMessages) {
            if (!recentLogEntries.contains(individualMessage)) {
                try {
                    FileWriter fileWriter = new FileWriter(this.logFile, true);
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write(individualMessage + "\n");
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("The message" + individualMessage + " is repeated!");
            }
        }
    }

    public List<String> get32MostRecentLogMessages() {
        File logFile = new File(this.logFile);
        List<String> log = new ArrayList<>();
        int counter = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String buf;
            while ((buf = reader.readLine()) != null && counter < 32) {
                log.add(buf);
                counter++;
            }
            reader.close();
            return log;
        } catch (FileNotFoundException e) {
            System.out.println("Error reading log File: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error closing log File: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getLog() {
        File logFile = new File(this.logFile);
        List<String> log = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String buf;
            while ((buf = reader.readLine()) != null) {
                log.add(buf);
            }
            reader.close();
            return log;
        } catch (FileNotFoundException e) {
            System.out.println("Error reading log File: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error closing log File: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
