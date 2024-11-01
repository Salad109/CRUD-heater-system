package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class Technician {
    private static final String READINGS_PATH = "data-readings.txt";
    private static final String ORDERS_PATH = "data-orders.txt";
    private static final String HEAT_PATH = "data-heat.txt";

    public void logMeterReading(Tenant tenant) throws TenantNotFoundException {
        int id = tenant.getId();
        double reading = getHeatByID(id);
        writeReadingToFile(id, reading);
    }

    public void logMeterReadings(List<Tenant> tenants) throws TenantNotFoundException {
        for (Tenant tenant : tenants) {
            logMeterReading(tenant);
        }
    }

    private double getHeatByID(int id) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(HEAT_PATH);
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        newLines.add(lines.getFirst());
        double heatValue = 0.0;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            String[] parts = line.split(", ");

            if (Integer.parseInt(parts[0]) == id) {
                found = true;
                heatValue = Double.parseDouble(parts[1]);
                line = id + ", 0.0";  // Overwrite with zero heat value
            }
            newLines.add(line);  // Store each processed line
        }

        if (!found) {
            throw new TenantNotFoundException(id);
        }

        // Write all lines back to the file
        DatabaseManager.writeToFile(newLines, HEAT_PATH);

        return heatValue;  // Return the original heat value
    }

    private void writeReadingToFile(int id, double reading) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(READINGS_PATH);
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        newLines.add(lines.getFirst());
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            // Split line into parts by commas and trim spaces
            String[] parts = line.split(", ");

            // Check if line starts with the specified ID
            if (Integer.parseInt(parts[0]) == id) {
                found = true;
                parts[1] = String.valueOf(reading + Double.parseDouble(parts[1]));

                // Join all parts back into a single line
                line = String.join(", ", parts);
            }
            newLines.add(line);
        }

        if (!found) {
            throw new TenantNotFoundException(id);
        }
        DatabaseManager.writeToFile(newLines, READINGS_PATH);
    }

    public void readOrders() {
        List<String> orders = DatabaseManager.readFile(ORDERS_PATH);
        for (String order : orders) {
            System.out.println(order);
        }
    }

    public void deleteFirstOrder() {
        List<String> orders = DatabaseManager.readFile(ORDERS_PATH);
        orders.removeFirst();
        DatabaseManager.writeToFile(orders, ORDERS_PATH);
    }
}
