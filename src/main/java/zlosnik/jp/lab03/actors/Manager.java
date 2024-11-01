package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final String ORDERS_PATH = "orders.txt";
    private static final String RENTS_PATH = "rents.txt";
    private static final String TENANTS_PATH = "tenants.txt";
    private static final double PRICE_PER_HEAT_UNIT = 100.0;

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_PATH, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTenant(String street, List<Double> heaterSizes) {
        List<String> files = new ArrayList<>();
        List<List<String>> fileContents = new ArrayList<>();
        files.add("heat.txt");
        files.add("readings.txt");
        files.add("rents.txt");
        files.add("tenants.txt");
        List<Integer> takenIds = new ArrayList<>();

        // Get file contents
        for (String file : files) {
            fileContents.add(DatabaseManager.readAllLines(file));
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

        // Remove tenants.txt from file list, treat it individually
        files.removeLast();
        fileContents.removeLast();
        List<String> tenantFileContents = DatabaseManager.readAllLines(TENANTS_PATH);

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
}
