package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Technician {
    List<Tenant> tenants;

    public Technician() {
        tenants = TenantReader.getTenants();
    }

    public void getMeterReading(Tenant tenant) {
        double reading = tenant.getAccumulatedHeat();
        int id = tenant.getId();
        writeReadingToFile(id, reading);
        tenant.resetGeneratedHeat();
    }

    public void getMeterReadings(List<Tenant> tenants) {
        for (Tenant tenant : tenants) {
            getMeterReading(tenant);
        }
    }

    private void writeReadingToFile(int id, double reading) {
        String filePath = "readings.txt";
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            if ((line = br.readLine()) != null) {
                lines.add(line);
            }

            while ((line = br.readLine()) != null) {
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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String modifiedLine : lines) {
                bw.write(modifiedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
