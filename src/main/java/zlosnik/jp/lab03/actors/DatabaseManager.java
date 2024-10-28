package zlosnik.jp.lab03.actors;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    List<Tenant> tenants;

    public DatabaseManager() {
        tenants = TenantReader.getTenants();
    }

    public void updateTenants() {
        tenants = TenantReader.getTenants();
    }

    public void printTenants() {
        for (Tenant tenant : tenants) {
            System.out.println(tenant);
        }
    }

    public Tenant getTenantByID(int id) {
        for (Tenant tenant : tenants) {
            if (tenant.getId() == id) {
                return tenant;
            }
        }
        return null;
    }

    public List<Tenant> getTenantsByStreet(String street) {
        List<Tenant> newTenants = new ArrayList<>();
        boolean found = false;
        for (Tenant tenant : tenants) {
            if (tenant.getStreet().equals(street)) {
                newTenants.add(tenant);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No street named " + street + " has been found");
        }
        return newTenants;
    }

    public List<Tenant> getAllTenants() {
        return tenants;
    }
}
