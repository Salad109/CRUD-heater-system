package zlosnik.jp.lab03.actors;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();

    // # ID, Street, heater sizes, accumulated heat, bill
    public Tenant(int id, String street, List<Double> heaterSizes) {
        this.id = id;
        this.street = street;
        for (Double size : heaterSizes) {
            this.heaters.add(new Heater(size));
        }
    }

    public int getId() {
        return id;
    }

    public void generateHeat() {
        double generatedHeat = 0;
        for (Heater heater : heaters) {
            generatedHeat += heater.size();
        }
        generatedHeat = Math.round(generatedHeat * 1000.0) / 1000.0; // Round to 3 decimal places
        logHeat(generatedHeat);
    }

    private void logHeat(double generatedHeat) {
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

    public Double getBill() {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> lines = DatabaseManager.readFile(dataPath);
        String line = lines.get(1);
        String[] parts = line.split(", ");
        return Double.parseDouble(parts[4]);
    }

    public void payBill(double payAmount) {
        Path dataPath = DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("data.txt");
        List<String> lines = DatabaseManager.readFile(dataPath);
        String line = lines.get(1);
        String[] parts = line.split(", ");
        double bill = Double.parseDouble(parts[4]) - payAmount;
        parts[4] = Double.toString(bill);
        line = String.join(", ", parts);
        lines.set(1, line);
        DatabaseManager.writeToFile(lines, dataPath);
    }

    public String getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return "Tenant[ID=" + id + ", Street=" + street + ", " + heaters + "]";
    }

    @Override
    public int compareTo(Tenant o) {
        return Integer.compare(id, o.id);
    }
}
