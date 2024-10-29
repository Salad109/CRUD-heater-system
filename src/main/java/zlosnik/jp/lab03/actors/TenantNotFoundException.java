package zlosnik.jp.lab03.actors;

public class TenantNotFoundException extends Exception {
    public TenantNotFoundException(int id) {
        super("Tenant with ID " + id + " not found.");
    }
}
