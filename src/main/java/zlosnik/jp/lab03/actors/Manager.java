package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {
    private static final String ORDERS_PATH = "orders.txt";
    private static final String RENTS_PATH = "rents.txt";
    private static final double PRICE_PER_HEAT_UNIT = 100.0;

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_PATH, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTenant(int id, String street) {
        List<String> files = new ArrayList<>();
        files.add("heat.txt");
        files.add("readings.txt");
        files.add("rents.txt");
        // files.add("tenants.txt"); // TODO OSOBNO

        for (String file : files) {
            List<String> lines = DatabaseManager.readAllLines(file);
        }
    }
}
