package zlosnik.jp.lab03.tenant;

import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();
    private double generatedHeat;

    public Tenant(String street, List<Heater> heaters) {
        this.street = street;
        this.heaters.addAll(heaters);
        generatedHeat = 0;
    }

    public double getGeneratedHeat() {
        return generatedHeat;
    }

    public void resetGeneratedHeat() {
        this.generatedHeat = 0;
    }

    public void generateHeat() {
        for (Heater heater : heaters) {
            generatedHeat += heater.generateHeat();
        }
    }

    @Override
    public String toString() {
        return "Tenant[Street=" + street + ", Heaters=" + heaters + "]";
    }

    @Override
    public int compareTo(Tenant o) {
        return this.street.compareTo(o.street);
    }
}
