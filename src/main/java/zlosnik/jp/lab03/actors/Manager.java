package zlosnik.jp.lab03.actors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {

    public void issueOrder(String order) {
        String filePath = "orders.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(order);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
