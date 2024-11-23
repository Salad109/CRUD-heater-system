package zlosnik.jp.lab03.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();
    private final double bill;

    // # ID, Street, heater sizes, bill
    public Tenant(int id, String street, List<Double> heaterSizes, double bill) {
        this.id = id;
        this.street = street;
        for (Double size : heaterSizes) {
            this.heaters.add(new Heater(size));
        }
        this.bill = bill;
    }

    public int getId() {
        return id;
    }

    public void generateHeat() throws IOException {
        double accumulatedHeat = 0;
        for (Heater heater : heaters) {
            accumulatedHeat += heater.size();
        }
        accumulatedHeat = Math.round(accumulatedHeat * 1000.0) / 1000.0; // Round to 3 decimal places
        logHeat(accumulatedHeat);
    }

    private void logHeat(double generatedHeat) throws IOException {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> lines = DatabaseManager.readFile(dataPath);
        String line = lines.get(1);
        String[] parts = line.split(", ");
        double heat = Double.parseDouble(parts[3]) + generatedHeat;
        parts[3] = Double.toString(heat);
        line = String.join(", ", parts);
        lines.set(1, line);
        DatabaseManager.writeToFile(lines, dataPath);
    }

    public Double getBill() throws IOException {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> lines = DatabaseManager.readFile(dataPath);
        String line = lines.get(1);
        String[] parts = line.split(", ");
        return Double.parseDouble(parts[4]);
    }

    public void payBill(double payAmount) throws IOException {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> lines = DatabaseManager.readFile(dataPath);
        String line = lines.get(1);
        String[] parts = line.split(", ");
        double newBill = Double.parseDouble(parts[4]) - payAmount;
        parts[4] = Double.toString(newBill);
        line = String.join(", ", parts);
        lines.set(1, line);
        DatabaseManager.writeToFile(lines, dataPath);
        logBill(payAmount);
    }

    private void logBill(double payAmount) throws IOException {
        Path logPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("payment-logs.txt");
        String logMessage = "Tenant paid " + payAmount + " at " + Instant.now();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logPath.toString(), true))) {
            bw.write(logMessage);
            bw.newLine();
        }
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Tenant[ID=" + id + ", Street=" + street + ", " + heaters + "], bill=" + bill + "]";
    }

    @Override
    public int compareTo(Tenant o) {
        return Integer.compare(id, o.id);
    }
}
