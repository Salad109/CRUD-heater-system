package zlosnik.jp.lab03.utils;

public class StreetNotFoundException extends Exception{
    StreetNotFoundException(String street) {
        super("Street " + street + " not found");
    }
}
