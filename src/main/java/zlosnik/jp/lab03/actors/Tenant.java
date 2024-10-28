package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class Tenant implements Comparable<Tenant> {
    private final int id;
    private final String street;
    private final List<Heater> heaters = new ArrayList<>();
    private double accumulatedHeat;

    public Tenant(int id, String street, List<Heater> heaters) {
        this.id = id;
        this.street = street;
        this.heaters.addAll(heaters);
    }

    public int getId() {
        return id;
    }

    public double getAccumulatedHeat() {
        return accumulatedHeat;
    }

    public void resetGeneratedHeat() {
        this.accumulatedHeat = 0;
    }

    public void generateHeat() {
        for (Heater heater : heaters) {
            accumulatedHeat += heater.generateHeat();
        }
        System.out.println("Generated heat: " + accumulatedHeat);
    }

    public String getStreet(){
        return street;
    }

    @Override
    public String toString() {
        return "Tenant[ID=" + id + ", Street=" + street + ", accumulated heat=" + accumulatedHeat + ", " + heaters + "]";
    }

    @Override
    public int compareTo(Tenant o) {
        return this.street.compareTo(o.street);
    }
}
