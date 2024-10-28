package zlosnik.jp.lab03.apps;

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

                int id = Integer.parseInt(parts[0]);

                String name = parts[1];
                if(name.startsWith(" "))
                    name = name.substring(1);

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
}
