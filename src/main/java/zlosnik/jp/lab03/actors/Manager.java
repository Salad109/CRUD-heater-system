package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {
    private static final String ORDERS_PATH = "orders.txt";
    private static final String RENTS_PATH = "rents.txt";
    private static final String TENANTS_PATH = "tenants.txt";
    private static final double PRICE_PER_HEAT_UNIT = 100.0;

    public void issueOrder(String order) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_PATH, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
