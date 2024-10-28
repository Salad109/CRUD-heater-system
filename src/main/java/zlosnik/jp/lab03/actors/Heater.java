package zlosnik.jp.lab03.actors;

public class Heater {
    double size;

    public Heater(double size) {
        this.size = size;
    }

    public double generateHeat() {
        return size;
    }

    @Override
    public String toString() {
        return "Heater[size=" + size + "]";
    }
}
