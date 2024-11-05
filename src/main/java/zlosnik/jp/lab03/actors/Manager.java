package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class Manager {
    private static final double PRICE_PER_HEAT_UNIT = 100.0;

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
            logBill(newBill, id);

            return newBill;
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
}
