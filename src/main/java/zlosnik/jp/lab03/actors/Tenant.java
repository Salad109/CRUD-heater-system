package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();
    private static final String HEAT_PATH = "data-heat.txt";

    public Tenant(int id, String street, List<Heater> heaters) {
        this.id = id;
        this.street = street;
        this.heaters.addAll(heaters);
    }

    public int getId() {
        return id;
    }

    public void generateHeat() throws TenantNotFoundException {
        double generatedHeat = 0;
        for (Heater heater : heaters) {
            generatedHeat += heater.size();
        }
        generatedHeat = Math.round(generatedHeat * 1000.0) / 1000.0; // Round to 3 decimal places
        logHeat(getId(), generatedHeat);
    }

    private void logHeat(int id, double generatedHeat) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(HEAT_PATH);
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        // Add header line
        newLines.add(lines.getFirst());

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(", ");

            if (Integer.parseInt(parts[0]) == id) {
                found = true;
                double newHeat = Double.parseDouble(parts[1]) + generatedHeat;
                line = id + ", " + newHeat;  // Update line with new heat value
            }

            newLines.add(line);  // Store the processed line
        }

        if (!found) {
            throw new TenantNotFoundException(id);
        }
        DatabaseManager.writeToFile(newLines, HEAT_PATH);
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
        return this.street.compareTo(o.street);
    }
}
