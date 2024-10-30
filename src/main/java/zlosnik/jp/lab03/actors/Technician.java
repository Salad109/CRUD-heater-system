package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Technician {
    private static final String READINGS_PATH = "readings.txt";
    private static final String ORDERS_PATH = "orders.txt";
    private static final String HEAT_PATH = "heat.txt";

    public void logMeterReading(Tenant tenant) {
        int id = tenant.getId();
        double reading = getHeatByID(id);
        writeReadingToFile(id, reading);
    }

    public void logMeterReadings(List<Tenant> tenants) {
        for (Tenant tenant : tenants) {
            logMeterReading(tenant);
        }
    }

    private List<String> readAllLines(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void writeToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private double getHeatByID(int id) {
        List<String> lines = readAllLines(HEAT_PATH);

        List<String> newLines = new ArrayList<>();
        double heatValue = 0.0;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            String[] parts = line.split(", ");

            if (Integer.parseInt(parts[0]) == id) {
                heatValue = Double.parseDouble(parts[1].trim());
                line = id + ", 0.0";  // Overwrite with zero heat value
            }
            newLines.add(line);  // Store each processed line
        }

        // Write all lines back to the file
        writeToFile(newLines, HEAT_PATH);

        return heatValue;  // Return the original heat value
    }

    private void writeReadingToFile(int id, double reading) {
        List<String> lines = readAllLines(READINGS_PATH);
        List<String> newLines = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // Split line into parts by commas and trim spaces
            String[] parts = line.split(", ");

            // Check if line starts with the specified ID
            if (Integer.parseInt(parts[0]) == id) {
                // Replace the last numeric part with the new reading
                String lastPart = parts[parts.length - 1];

                // Find where the last number starts in the line and replace it
                int lastSpace = lastPart.lastIndexOf(" ");
                if (lastSpace != -1) {
                    parts[parts.length - 1] = lastPart.substring(0, lastSpace) + " " + reading;
                } else {
                    parts[parts.length - 1] = String.valueOf(reading);
                }

                // Join all parts back into a single line
                line = String.join(", ", parts);
            }
            newLines.add(line);
        }

        writeToFile(newLines, READINGS_PATH);
    }

    public void readOrders() {
        try (BufferedReader orderReader = new BufferedReader(new FileReader(ORDERS_PATH))) {
            List<String> orders = orderReader.lines().toList();
            for (String order : orders) {
                System.out.println(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFirstOrder() {
        List<String> orders = new LinkedList<>();

        try (BufferedReader orderReader = new BufferedReader(new FileReader(ORDERS_PATH))) {
            orderReader.readLine();

            String line;
            while ((line = orderReader.readLine()) != null) {
                orders.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter orderWriter = new BufferedWriter(new FileWriter(ORDERS_PATH))) {
            for (String order : orders) {
                orderWriter.write(order);
                orderWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
