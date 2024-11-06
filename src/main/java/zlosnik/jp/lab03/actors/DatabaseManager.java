package zlosnik.jp.lab03.actors;

import java.io.*;
import java.nio.file.*;
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
        List<Tenant> tenants = refreshTenants();
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public static List<Tenant> refreshTenants() {
        List<Tenant> tenants = new ArrayList<>();
        try {
            List<String> folderNames = getFolderNames();
            for (String folderName : folderNames) {
                Tenant tenant = getTenantByID(Integer.parseInt(folderName));
                tenants.add(tenant);
            }
        } catch (IOException | TenantNotFoundException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        tenants.sort(Tenant::compareTo);

        return tenants;
    }

    public static void deleteTenant(int id) throws TenantNotFoundException {
        Path tenantDirPath = TENANTS_DIRECTORY.resolve(Integer.toString(id));

        // Walk through the file tree, deleting files and subdirectories
        try {
            Files.delete(tenantDirPath.resolve("data.txt"));
            Files.delete(tenantDirPath.resolve("payment-logs.txt"));
            Files.delete(tenantDirPath);
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
            double bill = Double.parseDouble(parts[4]);
            return new Tenant(id, street, heaterSizesParsed, bill);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public static List<Tenant> getTenantsByStreet(String street) throws StreetNotFoundException {
        List<Tenant> tenants = refreshTenants();
        List<Tenant> newTenants = new ArrayList<>();
        for (Tenant tenant : tenants) {
            if (tenant.getStreet().equals(street)) {
                newTenants.add(tenant);
            }
        }
        if (newTenants.isEmpty()) throw new StreetNotFoundException(street);
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
            newReadings.add(readings.getFirst());
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
