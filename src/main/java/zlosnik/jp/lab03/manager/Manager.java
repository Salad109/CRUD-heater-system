package zlosnik.jp.lab03.manager;

import zlosnik.jp.lab03.apps.TenantReader;
import zlosnik.jp.lab03.tenant.Tenant;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    List<Tenant> tenants;

    public Manager() {
        tenants = TenantReader.getTenants();
    }

    public void updateTenants() {
        tenants = TenantReader.getTenants();
    }

    public void printTenants() {
        if (tenants != null) {
            for (Tenant tenant : tenants) {
                System.out.println(tenant);
            }
        }
    }

    public List<Tenant> getAllTenants() {
        return tenants;
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
            System.out.println("No such street found");
        }
        return newTenants;
    }
}
