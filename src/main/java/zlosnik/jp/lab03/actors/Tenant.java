package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();
    private static final String HEAT_PATH = "heat.txt";

    public Tenant(int id, String street, List<Heater> heaters) {
        this.id = id;
        this.street = street;
        this.heaters.addAll(heaters);
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
        updateHeatById(getId(), generatedHeat);
    }

    private void updateHeatById(int id, double generatedHeat) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(HEAT_PATH))) {
            String line;

            // Read and add the header line
            if ((line = reader.readLine()) != null) {
                lines.add(line);  // Add header to the lines list
            }

            // Process each line to find the ID and update its heat value
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                if (Integer.parseInt(parts[0]) == id) {
                    double newHeat = Double.parseDouble(parts[1]) + generatedHeat;
                    line = id + ", " + newHeat;  // Update line with new heat value
                }

                lines.add(line);  // Store the processed line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write all lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HEAT_PATH))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
