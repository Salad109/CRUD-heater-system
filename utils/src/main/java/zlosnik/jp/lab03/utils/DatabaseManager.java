package zlosnik.jp.lab03.utils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DatabaseManager {
    private DatabaseManager() {
    }

    public static final Path TENANTS_DIRECTORY = Paths.get("data");
    public static final Path READINGS_PATH = TENANTS_DIRECTORY.resolve("readings.txt");
    public static final Path ORDERS_PATH = TENANTS_DIRECTORY.resolve("orders.txt");


    private static List<String> getFolderNames() throws IOException {
        List<String> folderNames = new ArrayList<>();

        // Get directory
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

    public static int getFreeId() throws IOException {
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

    public static void printTenants() {
        List<Tenant> tenants = getAllTenants();
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public static void deleteTenant(int id) throws TenantNotFoundException {
        Path tenantDirPath = TENANTS_DIRECTORY.resolve(Integer.toString(id));
        try {
            // Delete the files inside tenant folder
            Files.delete(tenantDirPath.resolve("data.txt"));
            Files.delete(tenantDirPath.resolve("payment-logs.txt"));
            // Delete tenant folder
            Files.delete(tenantDirPath);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public static Tenant getTenantByID(int id) throws TenantNotFoundException {
        Path dataPath = TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        try {
            // Read tenant information from their "data.txt"
            List<String> dataLines = DatabaseManager.readFile(dataPath);
            String[] parts = dataLines.get(1).split(", ");
            String street = parts[1];
            String[] heaterSizes = parts[2].split(" ");
            List<Double> heaterSizesParsed = new ArrayList<>();
            for (String heaterSize : heaterSizes) {
                heaterSizesParsed.add(Double.parseDouble(heaterSize));
            }
            double bill = Double.parseDouble(parts[4]);
            // Return newly built tenant instance
            return new Tenant(id, street, heaterSizesParsed, bill);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public static List<Tenant> getTenantsByStreet(String street) throws StreetNotFoundException {
        List<Tenant> tenants = getAllTenants();
        List<Tenant> newTenants = new ArrayList<>();
        for (Tenant tenant : tenants) {
            if (tenant.getStreet().equals(street)) {
                newTenants.add(tenant);
            }
        }
        if (newTenants.isEmpty()) throw new StreetNotFoundException(street);
        return newTenants;
    }

    public static List<Tenant> getAllTenants() {
        List<Tenant> tenants = new ArrayList<>();
        try {
            List<String> folderNames = getFolderNames();
            // Get every tenant
            for (String folderName : folderNames) {
                Tenant tenant = getTenantByID(Integer.parseInt(folderName));
                tenants.add(tenant);
            }
        } catch (IOException | TenantNotFoundException e) {
            return new ArrayList<>();
        }

        tenants.sort(Tenant::compareTo);

        return tenants;
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
}
