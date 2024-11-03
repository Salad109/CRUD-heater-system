package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DatabaseManager {
    public static final Logger logger = Logger.getLogger("Database manager logger");
    public static final String HEAT_PATH = "data-heat.txt";
    public static final String READINGS_PATH = "data-readings.txt";
    public static final String BILLS_PATH = "data-bills.txt";
    public static final String TENANTS_PATH = "data-tenants.txt";
    public static final String ORDERS_PATH = "data-orders.txt";


    public static void createTenant(String street, List<Double> heaterSizes) {
        List<String> files = new ArrayList<>();
        files.add(HEAT_PATH);
        files.add(READINGS_PATH);
        files.add(BILLS_PATH);
        files.add(TENANTS_PATH);
        List<Integer> takenIds = new ArrayList<>();

        // Get file contents
        List<List<String>> fileContents = new ArrayList<>();
        for (String file : files) {
            fileContents.add(DatabaseManager.readFile(file));
        }

        // Find all taken IDs
        for (List<String> lines : fileContents) {
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(", ");
                if (!takenIds.contains(Integer.parseInt(parts[0]))) takenIds.add(Integer.parseInt(parts[0]));
            }
        }

        // Find free ID to assign
        int freeId = 0;
        do {
            freeId++;
        } while (takenIds.contains(freeId));

        // Remove data-tenants.txt from file list, treat it individually
        files.removeLast();
        fileContents.removeLast();
        List<String> tenantFileContents = DatabaseManager.readFile(TENANTS_PATH);

        StringBuilder heaters = new StringBuilder();
        for (Double size : heaterSizes) {
            heaters.append(", ").append(size);
        }
        tenantFileContents.add(freeId + ", " + street + heaters);

        DatabaseManager.writeToFile(tenantFileContents, TENANTS_PATH);

        // Add line with new ID to all files
        for (List<String> lines : fileContents) {
            lines.add(freeId + ", 0.0");
        }

        // Write updated content to files
        for (int i = 0; i < files.size(); i++) {
            DatabaseManager.writeToFile(fileContents.get(i), files.get(i));
        }

        System.out.println("Added tenant: " + tenantFileContents.getLast());
    }


    public static void readTenants() {
        List<Tenant> tenants = updateTenants();
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }


    public static List<Tenant> updateTenants() {
        List<Tenant> tenants = new ArrayList<>();
        List<String> lines = DatabaseManager.readFile(TENANTS_PATH);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(", ");

            int id = Integer.parseInt(parts[0]);
            String street = parts[1];

            String[] heaterSizes = parts[2].split(" ");
            List<Double> heaterSizesParsed = new ArrayList<>();
            for (String heaterSize : heaterSizes) {
                heaterSizesParsed.add(Double.parseDouble(heaterSize));
            }

            tenants.add(new Tenant(id, street, heaterSizesParsed));
        }

        tenants.sort(Tenant::compareTo); // Sort alphabetically by street
        return tenants;
    }

    public static void deleteTenant(int id) throws TenantNotFoundException {
        List<String> files = new ArrayList<>();
        files.add(HEAT_PATH);
        files.add(READINGS_PATH);
        files.add(BILLS_PATH);
        files.add(TENANTS_PATH);

        boolean found = false;

        // Get file contents
        List<List<String>> fileContents = new ArrayList<>();
        for (String file : files) {
            fileContents.add(DatabaseManager.readFile(file));
        }

        for (List<String> lines : fileContents) {
            for (int i = lines.size() - 1; i > 0; i--) {
                String line = lines.get(i);
                String[] parts = line.split(", ");
                if (parts[0].equals(Integer.toString(id))) {
                    lines.remove(i);
                    found = true;
                }
            }
        }

        if (!found) throw new TenantNotFoundException(id);

        for (int i = 0; i < files.size(); i++) {
            DatabaseManager.writeToFile(fileContents.get(i), files.get(i));
        }
    }

    public static Tenant getTenantByID(int id) throws TenantNotFoundException {
        List<Tenant> tenants = updateTenants();
        for (Tenant tenant : tenants) {
            if (tenant.getId() == id) {
                return tenant;
            }
        }
        throw new TenantNotFoundException(id);
    }

    public static List<Tenant> getTenantsByStreet(String street) throws StreetNotFoundException {
        List<Tenant> tenants = updateTenants();
        List<Tenant> newTenants = new ArrayList<>();
        for (Tenant tenant : tenants)
            if (tenant.getStreet().equals(street)) newTenants.add(tenant);
        if (newTenants.isEmpty()) throw new StreetNotFoundException(street);
        return newTenants;
    }

    public static List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + fileName, e);
        }
        return lines;
    }

    public static void writeToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to file: " + fileName, e);
        }
    }

    public static double getReading(int id) throws TenantNotFoundException {
        List<String> readings = DatabaseManager.readFile(READINGS_PATH);
        List<String> newReadings = new ArrayList<>();
        newReadings.add(readings.getFirst());
        double reading = 0;
        boolean found = false;
        for (int i = 1; i < readings.size(); i++) {
            String line = readings.get(i);
            String[] parts = line.split(", ");
            if (Integer.parseInt(parts[0]) == id) {
                reading = Double.parseDouble(parts[1]);
                found = true;
                parts[1] = "0.0";
            }
            newReadings.add(String.join(", ", parts));
        }
        if (!found) throw new TenantNotFoundException(id);

        DatabaseManager.writeToFile(newReadings, READINGS_PATH);
        return reading;
    }
}
