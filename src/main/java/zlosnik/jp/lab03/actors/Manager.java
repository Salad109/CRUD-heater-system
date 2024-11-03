package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager {
    private static final double PRICE_PER_HEAT_UNIT = 100.0;
    private static final Logger logger = Logger.getLogger("Manager logger");

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DatabaseManager.ORDERS_PATH, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + DatabaseManager.ORDERS_PATH, e);
        }
    }

    private double calculateBill(double reading) {
        return reading * PRICE_PER_HEAT_UNIT;
    }

    public double billTenant(int id) throws TenantNotFoundException {
        double reading = DatabaseManager.getReading(id);
        double due = calculateBill(reading);
        List<String> billList = DatabaseManager.readFile(DatabaseManager.BILLS_PATH);
        List<String> newBills = new ArrayList<>();
        newBills.add(billList.getFirst());
        boolean found = false;

        for (int i = 1; i < billList.size(); i++) {
            String line = billList.get(i);
            String[] parts = line.split(", ");
            if (Integer.parseInt(parts[0]) == id) {
                due += Double.parseDouble(parts[1]); // Add new due to existing due
                found = true;
                parts[1] = Double.toString(due);
            }
            newBills.add(String.join(", ", parts));
        }
        if (!found) throw new TenantNotFoundException(id);

        DatabaseManager.writeToFile(newBills, DatabaseManager.BILLS_PATH);

        return due;
    }
}
