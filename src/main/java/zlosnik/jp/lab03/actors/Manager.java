package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final String ORDERS_PATH = "data-orders.txt";
    private static final String RENTS_PATH = "data-rents.txt";
    private static final double PRICE_PER_HEAT_UNIT = 100.0;

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_PATH, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateRent(double reading) {
        return reading * PRICE_PER_HEAT_UNIT;
    }

    public double billTenant(int id) throws TenantNotFoundException {
        double reading = DatabaseManager.getReading(id);
        double due = calculateRent(reading);
        List<String> rents = DatabaseManager.readFile(RENTS_PATH);
        List<String> newRents = new ArrayList<>();
        newRents.add(rents.getFirst());
        boolean found = false;

        for (int i = 1; i < rents.size(); i++) {
            String line = rents.get(i);
            String[] parts = line.split(", ");
            if (Integer.parseInt(parts[0]) == id) {
                due += Double.parseDouble(parts[1]); // Add new due rent to existing due
                found = true;
                parts[1] = Double.toString(due);
            }
            newRents.add(String.join(", ", parts));
        }
        if (!found) throw new TenantNotFoundException(id);

        DatabaseManager.writeToFile(newRents, RENTS_PATH);

        return due;
    }
}
