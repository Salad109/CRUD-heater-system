package zlosnik.jp.lab03.actors;

import java.util.List;

public interface TechnicianDao {
    void addTenant(Tenant tenant);

    void addTenants(List<Tenant> tenants);

    List<Tenant> getTenants();

    void replaceTenants(List<Tenant> tenants);

    void deleteTenants();
}
