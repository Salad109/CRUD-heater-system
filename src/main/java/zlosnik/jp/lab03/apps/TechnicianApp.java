package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.List;

public class TechnicianApp {
    public static void main(String[] args) {
        Technician technician = new Technician();
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        do {
            int n = 0;
            System.out.println("Technician app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read orders");
            System.out.println(n++ + ". Delete oldest order");
            System.out.println(n++ + ". Read a specific tenant");
            System.out.println(n++ + ". Read a specific street");
            System.out.println(n + ". Read all tenants");
            try {
                choice = scanner.readLine();
                switch (choice) {
                    case "0":
                        scanner.close();
                        break;
                    case "1":
                        readOrders(technician);
                        break;
                    case "2":
                        deleteOrder(technician);
                        break;
                    case "3":
                        readID(scanner, technician);
                        break;
                    case "4":
                        readStreet(scanner, technician);
                        break;
                    case "5":
                        readAll(technician);
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Input exception.");
            }
        } while (!choice.equals("0"));
    }

    private static void readOrders(Technician technician) {
        try {
            technician.readOrders();
        } catch (IOException e) {
            System.out.println("Failed to read orders.");
        }
    }

    private static void deleteOrder(Technician technician) {
        try {
            System.out.println("Deleting oldest order...");
            technician.deleteOldestOrder();
            System.out.println("Oldest order deleted!");
        } catch (IOException e) {
            System.out.println("Failed to delete oldest order.");
        }
    }

    private static void readID(BufferedReader scanner, Technician technician) {
        System.out.println("Provide tenant ID:");
        try {
            int id = Integer.parseInt(scanner.readLine());
            System.out.println("Reading tenant...");
            Tenant tenant = DatabaseManager.getTenantByID(id);
            technician.logMeterReading(tenant);
            System.out.println("Tenant read complete!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        } catch (IOException e) {
            System.out.println("Failed to log meter reading.");
        }
    }

    private static void readStreet(BufferedReader scanner, Technician technician) {
        try {
            System.out.println("Provide street:");
            String street = scanner.readLine();
            System.out.println("Reading street...");
            List<Tenant> tenantList = DatabaseManager.getTenantsByStreet(street);
            technician.logMeterReadings(tenantList);
            System.out.println("Street read complete!");
        } catch (StreetNotFoundException e) {
            System.out.println("Tenant not found.");
        } catch (IOException e) {
            System.out.println("Failed to log meter reading.");
        }
    }

    private static void readAll(Technician technician) {
        try {
            System.out.println("Reading all tenants...");
            technician.logMeterReadings(DatabaseManager.refreshTenants());
            System.out.println("All tenants read complete!");
        } catch (StreetNotFoundException e) {
            System.out.println("Error reading all tenants.");
        } catch (IOException e) {
            System.out.println("Failed to log meter reading.");
        }
    }
}