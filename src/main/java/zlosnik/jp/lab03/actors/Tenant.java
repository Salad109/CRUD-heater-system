package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();

    public Tenant(int id, String street, List<Double> heaterSizes) {
        this.id = id;
        this.street = street;
        for (Double size : heaterSizes) {
            this.heaters.add(new Heater(size));
        }
    }

    public int getId() {
        return id;
    }

    public void generateHeat() throws TenantNotFoundException {
        double generatedHeat = 0;
        for (Heater heater : heaters) {
            generatedHeat += heater.size();
        }
        generatedHeat = Math.round(generatedHeat * 1000.0) / 1000.0; // Round to 3 decimal places
        logHeat(getId(), generatedHeat);
    }

    private void logHeat(int id, double generatedHeat) throws TenantNotFoundException {
        List<String> lines = DatabaseManager.readFile(DatabaseManager.HEAT_PATH);
        List<String> newLines = new ArrayList<>();
        boolean found = false;

        // Add header line
        newLines.add(lines.getFirst());

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(", ");

            if (Integer.parseInt(parts[0]) == id) {
                found = true;
                parts[1] = String.valueOf(Double.parseDouble(parts[1]) + generatedHeat); // Update line with new heat value
            }

            newLines.add(String.join(", ", parts));  // Store the processed line
        }

        if (!found) throw new TenantNotFoundException(id);
        DatabaseManager.writeToFile(newLines, DatabaseManager.HEAT_PATH);
    }

    public Double getBill() throws TenantNotFoundException {
        List<String> billList = DatabaseManager.readFile(DatabaseManager.BILLS_PATH);
        Double bill = null;
        for (int i = 1; i < billList.size(); i++) {
            String line = billList.get(i);
            String[] parts = line.split(", ");
            if (Integer.parseInt(parts[0]) == getId()) {
                bill = Double.parseDouble(parts[1]);
                break;
            }
        }
        if (bill == null) {
            throw new TenantNotFoundException(getId());
        }
        return bill;
    }

    public void payBill(double payAmount) throws TenantNotFoundException {
        List<String> billList = DatabaseManager.readFile(DatabaseManager.BILLS_PATH);
        List<String> newBillList = new ArrayList<>();
        boolean found = false;
        newBillList.add(billList.getFirst());

        for (int i = 1; i < billList.size(); i++) {
            String line = billList.get(i);
            String[] parts = line.split(", ");
            if (Integer.parseInt(parts[0]) == getId()) {
                double bill = Double.parseDouble(parts[1]) - payAmount;
                parts[1] = Double.toString(bill);
                found = true;
            }
            newBillList.add(String.join(", ", parts));
        }

        if (!found) throw new TenantNotFoundException(getId());

        DatabaseManager.writeToFile(newBillList, DatabaseManager.BILLS_PATH);
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
