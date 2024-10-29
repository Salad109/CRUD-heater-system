package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Technician {
    private static final String READINGS_PATH = "readings.txt";
    private static final String ORDERS_PATH = "orders.txt";

    public void getMeterReading(Tenant tenant) {
        double reading = tenant.getAccumulatedHeat();
        int id = tenant.getId();
        writeReadingToFile(id, reading);
        tenant.resetAccumulatedHeat();
    }

    public void getMeterReadings(List<Tenant> tenants) {
        for (Tenant tenant : tenants) {
            getMeterReading(tenant);
        }
    }

    private void writeReadingToFile(int id, double reading) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(READINGS_PATH))) {
            String line;

            if ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0].trim()) == id) {
                    int lastSpace = parts[parts.length - 1].lastIndexOf(" ");
                    parts[parts.length - 1] = lastSpace != -1 ? parts[parts.length - 1].substring(0, lastSpace).trim() + " " + reading : String.valueOf(reading);
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(READINGS_PATH))) {
            for (String modifiedLine : lines) {
                writer.write(modifiedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
