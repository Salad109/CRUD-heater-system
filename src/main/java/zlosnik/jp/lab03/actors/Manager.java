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
            logger.log(Level.SEVERE, "Error reading file: " + DatabaseManager.ORDERS_PATH, e);
        }
    }

    private double calculateBill(double reading) {
        return reading * PRICE_PER_HEAT_UNIT;
    }

    public double billTenant(int id) throws TenantNotFoundException {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id));
        dataPath = dataPath.resolve(id + ".txt");
        List<String> dataFileContents = DatabaseManager.readFile(dataPath);
        String dataLine = dataFileContents.get(1);
        String[] parts = dataLine.split(", ");
        double newBill = Double.parseDouble(parts[3]) + calculateBill(DatabaseManager.getReading(id));
        parts[3] = Double.toString(newBill);
        dataLine = String.join(", ", parts);
        dataFileContents.set(1, dataLine);
        DatabaseManager.writeToFile(dataFileContents, dataPath);

        return newBill;
    }

}
