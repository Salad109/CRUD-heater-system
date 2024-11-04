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
    public static final Path TENANTS_DIRECTORY = Paths.get("tenants");
    public static final Path ORDERS_PATH = Paths.get("data-orders.txt");


    public static void createTenant(String street, List<Double> heaterSizes) throws IOException {
        // Get unused ID
        int id = getFreeId();
        // Create a folder and files for the new tenant, get path to their data.txt
        Path dataPath = createDirectoryAndFiles(id);

        List<String> lines = new ArrayList<>();
        // Header line
        lines.add("# ID, Street, heater sizes, accumulated heat, bill");
        // Build the data line
        StringBuilder dataLine = new StringBuilder((id + ", " + street + ","));
        for (Double heaterSize : heaterSizes) {
            dataLine.append(" ").append(heaterSize);
        }
        dataLine.append(", 0.0, 0.0");

        lines.add(dataLine.toString());
        // Fill the data.txt with tenant's information
        writeToFile(lines, dataPath);
    }

    private static List<String> getFolderNames() throws IOException {
        List<String> folderNames = new ArrayList<>();

        // Open the directory stream
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(TENANTS_DIRECTORY)) {
            // Iterate over each entry in the directory
            for (Path entry : stream) {
                // Check if the entry is a directory
                if (Files.isDirectory(entry)) {
                    // Add the directory name to the list
                    folderNames.add(entry.getFileName().toString());
                }
            }
        }

        return folderNames;
    }

    private static int getFreeId() throws IOException {
        // Get all tenants
        List<String> folderNames = getFolderNames();
        List<Integer> folderIDs = new ArrayList<>();
        // Convert names into int
        for (String folderName : folderNames) {
            folderIDs.add(Integer.parseInt(folderName));
        }
        int freeId = 1;
        // Iterate to locate the smallest unused int
        while (folderIDs.contains(freeId)) {
            freeId++;
        }
        return freeId;
    }

    private static Path createDirectoryAndFiles(int id) throws IOException {
        // Create a directory with the provided ID
        Path dirPath = TENANTS_DIRECTORY.resolve(Integer.toString(id));
        Files.createDirectories(dirPath);

        // Create data.txt file inside the directory
        Path dataPath = dirPath.resolve("data.txt");
        Files.createFile(dataPath);
        // Create a payment log file
        Path logsPath = dirPath.resolve("payment-logs.txt");
        Files.createFile(logsPath);

        // Return path to data.txt
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
                double heat = Double.parseDouble(parts[3]);
                double bill = Double.parseDouble(parts[4]);
                tenants.add(new Tenant(id, street, heaterSizesParsed, heat, bill));
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

    public static Tenant getTenantByID(int id) throws TenantNotFoundException {
        Path dataPath = TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        try {
            List<String> dataLines = DatabaseManager.readFile(dataPath);
            String[] parts = dataLines.get(1).split(", ");
            String street = parts[1];
            String[] heaterSizes = parts[2].split(" ");
            List<Double> heaterSizesParsed = new ArrayList<>();
            for (String heaterSize : heaterSizes) {
                heaterSizesParsed.add(Double.parseDouble(heaterSize));
            }
            double accumulatedHeat = Double.parseDouble(parts[3]);
            double bill = Double.parseDouble(parts[4]);
            return new Tenant(id, street, heaterSizesParsed, accumulatedHeat, bill);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public static List<Tenant> getTenantsByStreet(String wantedStreet) {
        List<Tenant> tenants = updateTenants();
        List<Tenant> newTenants = new ArrayList<>();
        for (Tenant tenant : tenants) {
            if (tenant.getStreet().equals(wantedStreet)) {
                newTenants.add(tenant);
            }
        }
        return newTenants;
    }

    public static List<String> readFile(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        }
    }

    public static void writeToFile(List<String> lines, Path filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static double getReading(int id) throws TenantNotFoundException {
        try {
            List<String> readings = DatabaseManager.readFile(DatabaseManager.READINGS_PATH);
            List<String> newReadings = new ArrayList<>();
            double reading = 0;
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
            return reading;
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }
}
