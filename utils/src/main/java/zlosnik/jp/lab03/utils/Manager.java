package zlosnik.jp.lab03.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final double PRICE_PER_HEAT_UNIT = 100.0;
    private Tenant tenant = null;

    public void issueOrder(String order) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DatabaseManager.ORDERS_PATH.toString(), true))) {
            bw.write(order);
            bw.newLine();
        }
    }

    private double calculateBill(double reading) {
        return reading * PRICE_PER_HEAT_UNIT;
    }

    public double billTenant(int id) throws TenantNotFoundException {
        try {
            double reading = getReading(id);
            double newBill = calculateBill(reading);

            Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
            List<String> lines = DatabaseManager.readFile(dataPath);
            String dataLine = lines.get(1);
            String[] parts = dataLine.split(", ");

            double existingBill = Double.parseDouble(parts[4]);
            double totalBill = existingBill + newBill;

            parts[4] = Double.toString(totalBill);
            dataLine = String.join(", ", parts);
            lines.set(1, dataLine);

            DatabaseManager.writeToFile(lines, dataPath);
            logBill(newBill, id);

            return newBill;
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
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

    private void logBill(double payAmount, int id) throws IOException {
        Path logPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("payment-logs.txt");
        String logMessage = "Manager billed " + payAmount + " at " + Instant.now();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true))) {
            bw.write(logMessage);
            bw.newLine();
        }
    }

    public void setTenant(int id) throws TenantNotFoundException {
        tenant = DatabaseManager.getTenantByID(id);
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void updateStreet(String newStreet) throws TenantNotFoundException {
        int id = tenant.getId();
        try {
            Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
            List<String> lines = DatabaseManager.readFile(dataPath);
            String dataLine = lines.get(1);
            String[] parts = dataLine.split(", ");
            parts[1] = newStreet;
            dataLine = String.join(", ", parts);
            lines.set(1, dataLine);
            DatabaseManager.writeToFile(lines, dataPath);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public void updateHeaters(List<Double> heaterSizes) throws TenantNotFoundException {
        int id = tenant.getId();
        try {
            Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
            List<String> lines = DatabaseManager.readFile(dataPath);
            String dataLine = lines.get(1);
            String[] parts = dataLine.split(", ");
            StringBuilder heaterPart = new StringBuilder();
            for (Double heaterSize : heaterSizes) {
                heaterPart.append(heaterSize);
                heaterPart.append(" ");
            }
            heaterPart.deleteCharAt(heaterPart.length() - 1);
            parts[2] = heaterPart.toString();
            dataLine = String.join(", ", parts);
            lines.set(1, dataLine);
            DatabaseManager.writeToFile(lines, dataPath);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public void cleanBill() throws TenantNotFoundException {
        int id = tenant.getId();
        try {
            Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
            List<String> lines = DatabaseManager.readFile(dataPath);
            String dataLine = lines.get(1);
            String[] parts = dataLine.split(", ");
            parts[4] = "0.0";
            dataLine = String.join(", ", parts);
            lines.set(1, dataLine);
            DatabaseManager.writeToFile(lines, dataPath);
        } catch (IOException e) {
            throw new TenantNotFoundException(id);
        }
    }

    public void createTenant(String street, List<Double> heaterSizes) throws IOException {
        // Get unused ID
        int id = DatabaseManager.getFreeId();
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
        DatabaseManager.writeToFile(lines, dataPath);
    }

    private Path createDirectoryAndFiles(int id) throws IOException {
        // Create a directory with the provided ID
        Path dirPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id));
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

}
