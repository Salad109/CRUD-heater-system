package zlosnik.jp.lab03.actors;

public record Heater(double size) {

    @Override
    public String toString() {
        return "Heater[size=" + size + "]";
    }
}
