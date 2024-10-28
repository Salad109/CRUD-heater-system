package zlosnik.jp.lab03.technician;

import zlosnik.jp.lab03.tenant.Tenant;

import java.util.ArrayList;
import java.util.List;

public class Technician implements TechnicianDao {
    private List<Tenant> tenants = new ArrayList<>();

    public void addTenant(Tenant tenant) {
        tenants.add(tenant);
    }

    public void addTenants(List<Tenant> tenants) {
        this.tenants.addAll(tenants);
    }

    public Tenant getTenant(int id) {
        return tenants.get(id);
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void replaceTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    public void deleteTenants() {
        tenants.clear();
    }

    public double getMeterReading(int tenantId) {
        Tenant tenant = getTenant(tenantId);
        double reading = tenant.getGeneratedHeat();
        tenant.resetGeneratedHeat();
        return reading;
    }

    public List<Double> getMeterReadings() {
        List<Double> readings = new ArrayList<>(tenants.size());
        for (Tenant tenant : tenants) {
            readings.add(tenant.getGeneratedHeat());
        }
        return readings;
    }
}
