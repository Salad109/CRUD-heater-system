package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Manager {
    private static final double PRICE_PER_HEAT_UNIT = 100.0;
    private static final Logger logger = Logger.getLogger("Manager logger");

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DatabaseManager.ORDERS_PATH.toString(), true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            String errorMessage = "Error reading file: " + DatabaseManager.ORDERS_PATH;
            logger.log(Level.SEVERE, errorMessage, e);
        }
    }

    private double calculateBill(double reading) {
        return reading * PRICE_PER_HEAT_UNIT;
    }

    public double billTenant(int id) throws TenantNotFoundException, IOException {
        double reading = DatabaseManager.getReading(id);
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
        return newBill;
    }
}
