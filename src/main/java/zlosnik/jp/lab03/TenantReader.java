package zlosnik.jp.lab03;

import zlosnik.jp.lab03.tenant.Heater;
import zlosnik.jp.lab03.tenant.Tenant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class TenantReader {

    private TenantReader() {
    }

    public static List<Tenant> getTenants() {
        List<Tenant> tenants = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File("tenants.txt"))) {
            if (scanner.hasNextLine()) scanner.nextLine(); // Skip header line

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String name = parts[0];

                List<Heater> heaters = new ArrayList<>();
                for (String sizeStr : parts[1].trim().split(" ")) {
                    if (!sizeStr.isEmpty()) {
                        heaters.add(new Heater(Double.parseDouble(sizeStr)));
                    }
                }
                tenants.add(new Tenant(name, heaters));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tenants.sort(Tenant::compareTo); // Sort alphabetically by street
        return tenants;
    }
}
