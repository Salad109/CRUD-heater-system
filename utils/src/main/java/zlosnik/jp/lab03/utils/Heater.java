package zlosnik.jp.lab03.utils;

public record Heater(double size) {

    @Override
    public String toString() {
        return "Heater[size=" + size + "]";
    }
}
