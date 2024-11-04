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

    public double billTenant(int id) throws TenantNotFoundException, IOException { // TODO do wyjebania, ma być na podstawie data-readings.txt
        List<String> readings = DatabaseManager.readFile(DatabaseManager.READINGS_PATH);
        for(int i = 1; i < readings.size(); i++) {}
        return 0.0;
    }
}
