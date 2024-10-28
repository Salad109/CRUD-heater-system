package zlosnik.jp.lab03.manager;

import zlosnik.jp.lab03.TenantReader;
import zlosnik.jp.lab03.tenant.Tenant;

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
}
