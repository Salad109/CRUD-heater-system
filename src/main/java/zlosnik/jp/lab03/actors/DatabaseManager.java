package zlosnik.jp.lab03.actors;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseManager {
    private static final String TENANTS_PATH = "tenants.txt";
    private List<Tenant> tenants;

    public DatabaseManager() {
        tenants = updateTenants();
    }

    public void printTenants() {
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public Tenant getTenantByID(int id) throws TenantNotFoundException {
        for (Tenant tenant : tenants) {
            if (tenant.getId() == id) {
                return tenant;
            }
        }
        throw new TenantNotFoundException(id);
    }

    public List<Tenant> getTenantsByStreet(String street) throws StreetNotFoundException {
        List<Tenant> newTenants = new ArrayList<>();
        for (Tenant tenant : tenants)
            if (tenant.getStreet().equals(street)) newTenants.add(tenant);
        if (newTenants.isEmpty()) throw new StreetNotFoundException(street);
        return newTenants;
    }

    public List<Tenant> getAllTenants() {
        return tenants;
    }


    public List<Tenant> updateTenants() {
        tenants = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(TENANTS_PATH))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Skip header line

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");

                int id = Integer.parseInt(parts[0]);

                String name = parts[1];
                if (name.startsWith(" ")) name = name.substring(1);

                List<Heater> heaters = new ArrayList<>();
                for (String sizeStr : parts[2].split(" ")) {
                    if (!sizeStr.isEmpty()) {
                        heaters.add(new Heater(Double.parseDouble(sizeStr)));
                    }
                }

                tenants.add(new Tenant(id, name, heaters));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tenants.sort(Tenant::compareTo); // Sort alphabetically by street
        return tenants;
    }

    public static List<String> readAllLines(String fileName) {
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

    public static void writeToFile(List<String> lines, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
