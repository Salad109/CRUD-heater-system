package zlosnik.jp.lab03.actors;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DatabaseManager {
    private DatabaseManager() {
    }

    public static final Logger logger = Logger.getLogger("Database manager logger");
    public static final Path READINGS_PATH = Paths.get("data-readings.txt");
    public static final Path TENANTS_DIRECTORY = Paths.get("tenants/");
    public static final Path ORDERS_PATH = Paths.get("data-orders.txt");


    public static void createTenant(String street, List<Double> heaterSizes) throws IOException {
        int id = getFreeId();
        Path dataPath = createDirectoryAndFile(id);

        List<String> lines = new ArrayList<>();
        // Header line
        lines.add("# ID, Street, heater sizes, accumulated heat, bill");
        // Build the data line
        StringBuilder dataLine = new StringBuilder((id + ", " + street + ", "));
        for (Double heaterSize : heaterSizes) {
            dataLine.append(heaterSize).append(" ");
        }
        dataLine.append(", 0.0, 0.0");
        lines.add(dataLine.toString());

        writeToFile(lines, dataPath);
    }

    private static List<String> getFolderNames() throws IOException {
        List<String> folderNames = new ArrayList<>();

        // List all entries in the directory
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(TENANTS_DIRECTORY)) {

            // Iterate over each entry and check if it’s a directory
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    folderNames.add(entry.getFileName().toString()); // Add folder name to the list
                }
            }
        }

        return folderNames;
    }

    private static int getFreeId() throws IOException {
        List<String> folderNames = getFolderNames();
        List<Integer> folderIDs = new ArrayList<>();
        for (String folderName : folderNames) {
            folderIDs.add(Integer.parseInt(folderName));
        }
        int freeId = 1;
        while (folderIDs.contains(freeId)) {
            freeId++;
        }
        return freeId;
    }

    private static Path createDirectoryAndFile(int id) throws IOException {
        // Create a directory with the provided name
        Path dirPath = TENANTS_DIRECTORY.resolve(Integer.toString(id));
        Files.createDirectories(dirPath);

        // Create a .txt file inside the directory with the same name
        Path dataPath = dirPath.resolve("data.txt");
        Files.createFile(dataPath);
        Path logsPath = dirPath.resolve("payment-logs.txt");
        Files.createFile(logsPath);

        return dataPath;
    }

    public static void readTenants() {
        List<Tenant> tenants = updateTenants();
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public static List<Tenant> updateTenants() {
        List<Tenant> tenants = new ArrayList<>();
        try {
            List<String> folderNames = getFolderNames();
            for (String folderName : folderNames) {
                Path dataPath = TENANTS_DIRECTORY.resolve(folderName).resolve("data.txt");
                List<String> dataLines = DatabaseManager.readFile(dataPath);
                String dataLine = dataLines.get(1);
                // # ID, Street, heater sizes
                String[] parts = dataLine.split(", ");
                int id = Integer.parseInt(parts[0]);
                String street = parts[1];
                String[] heaterSizes = parts[2].split(" ");
                List<Double> heaterSizesParsed = new ArrayList<>();
                for (String heaterSize : heaterSizes) {
                    heaterSizesParsed.add(Double.parseDouble(heaterSize));
                }
                tenants.add(new Tenant(id, street, heaterSizesParsed));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        tenants.sort(Tenant::compareTo);

        return tenants;
    }

    public static void deleteTenant(int id) throws TenantNotFoundException {
        Path path = Paths.get("tenants/" + id);

        // Walk through the file tree, deleting files and subdirectories
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public static Tenant getTenantByID(int id) {
        Path dataPath = TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> dataLines = DatabaseManager.readFile(dataPath);
        String[] parts = dataLines.get(1).split(", ");
        String street = parts[1];
        String[] heaterSizes = parts[2].split(" ");
        List<Double> heaterSizesParsed = new ArrayList<>();
        for (String heaterSize : heaterSizes) {
            heaterSizesParsed.add(Double.parseDouble(heaterSize));
        }
        return new Tenant(id, street, heaterSizesParsed);
    }

    public static List<Tenant> getTenantsByStreet(String wantedStreet) {
        List<Tenant> tenants = new ArrayList<>();
        try {
            List<String> folderNames = getFolderNames();
            for (String folderName : folderNames) {
                Path dataPath = TENANTS_DIRECTORY.resolve(folderName).resolve("data.txt");
                List<String> dataLines = DatabaseManager.readFile(dataPath);
                String dataLine = dataLines.get(1);
                String[] parts = dataLine.split(", ");
                int id = Integer.parseInt(parts[0]);
                String street = parts[1];
                String[] heaterSizes = parts[2].split(" ");
                List<Double> heaterSizesParsed = new ArrayList<>();
                for (String heaterSize : heaterSizes) {
                    heaterSizesParsed.add(Double.parseDouble(heaterSize));
                }
                if (street.equals(wantedStreet)) {
                    tenants.add(new Tenant(id, street, heaterSizesParsed));
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return tenants;
    }

    public static List<String> readFile(Path filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + filePath, e);
        }
        return lines;
    }

    public static void writeToFile(List<String> lines, Path filePath) { // TODO throw exception
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to file: " + filePath, e);
        }
    }


    public static double getReading(int id) throws TenantNotFoundException {
        List<String> readings = DatabaseManager.readFile(DatabaseManager.READINGS_PATH);
        List<String> newReadings = new ArrayList<>();
        Double reading = null;
        for (int i = 1; i < readings.size(); i++) {
            String line = readings.get(i);
            String[] parts = line.split(", ");
            if (parts[0].equals(Integer.toString(id))) {
                reading = Double.parseDouble(parts[1]);
                parts[1] = "0.0";
            }
            newReadings.add(String.join(", ", parts));
        }

        DatabaseManager.writeToFile(newReadings, DatabaseManager.READINGS_PATH);

        if (reading != null) {
            return reading;
        } else {
            throw new TenantNotFoundException(id);
        }
    }
}
