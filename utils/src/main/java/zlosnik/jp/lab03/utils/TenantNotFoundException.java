package zlosnik.jp.lab03.utils;

public class TenantNotFoundException extends Exception {
    public TenantNotFoundException(int id) {
        super("Tenant with ID " + id + " not found.");
    }
}
