package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class Technician {
    public void logMeterReading(Tenant tenant) throws TenantNotFoundException {
        int id = tenant.getId();
        double reading = getHeatByID(id);
        writeReadingToFile(id, reading);
    }

    public void logMeterReadings(List<Tenant> tenants) throws StreetNotFoundException {
        for (Tenant tenant : tenants) {
            try {
                logMeterReading(tenant);
            } catch (TenantNotFoundException e) {
                throw new StreetNotFoundException(tenant.getStreet());
            }
        }
    }

    private double getHeatByID(int id) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(DatabaseManager.HEAT_PATH);
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

        if (!found) throw new TenantNotFoundException(id);

        // Write all lines back to the file
        DatabaseManager.writeToFile(newLines, DatabaseManager.HEAT_PATH);

        return heatValue;  // Return the original heat value
    }

    private void writeReadingToFile(int id, double reading) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(DatabaseManager.READINGS_PATH);
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

        if (!found) throw new TenantNotFoundException(id);
        DatabaseManager.writeToFile(newLines, DatabaseManager.READINGS_PATH);
    }

    public void readOrders() {
        List<String> orders = DatabaseManager.readFile(DatabaseManager.ORDERS_PATH);
        for (String order : orders) {
            System.out.println(order);
        }
    }

    public void deleteFirstOrder() {
        List<String> orders = DatabaseManager.readFile(DatabaseManager.ORDERS_PATH);
        orders.removeFirst();
        DatabaseManager.writeToFile(orders, DatabaseManager.ORDERS_PATH);
    }
}
