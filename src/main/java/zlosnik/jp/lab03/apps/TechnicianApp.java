package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TechnicianApp {
    public static void main(String[] args) {
        Technician technician = new Technician();
        var databaseManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);

        String choice;
        do {
            int n = 0;
            System.out.println("Technician app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read orders");
            System.out.println(n++ + ". Delete oldest order");
            System.out.println(n++ + ". Read a specific tenant");
            System.out.println(n++ + ". Read a specific street");
            System.out.println(n + ". Read all tenants");
            choice = scanner.nextLine();
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
                    readID(scanner, databaseManager, technician);
                    break;
                case "4":
                    readStreet(scanner, databaseManager, technician);
                    break;
                case "5":
                    readAll(databaseManager, technician);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!choice.equals("0"));
    }

    private static void readOrders(Technician technician) {
        technician.readOrders();
    }

    private static void deleteOrder(Technician technician) {
        System.out.println("Deleting oldest order...");
        technician.deleteFirstOrder();
        System.out.println("Oldest order deleted!");
    }

    private static void readID(Scanner scanner, DatabaseManager databaseManager, Technician technician) {
        System.out.println("Provide tenant ID:");
        try {
            String input = scanner.nextLine();
            int id = Integer.parseInt(input);
            System.out.println("Reading tenant...");
            Tenant tenant = databaseManager.getTenantByID(id);
            technician.logMeterReading(tenant);
            System.out.println("Tenant read complete!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        }
    }

    private static void readStreet(Scanner scanner, DatabaseManager databaseManager, Technician technician) {
        try {
            System.out.println("Provide street:");
            String street = scanner.nextLine();
            System.out.println("Reading street...");
            List<Tenant> tenantList = databaseManager.getTenantsByStreet(street);
            technician.logMeterReadings(tenantList);
            System.out.println("Street read complete!");
        } catch (StreetNotFoundException | TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        }
    }

    private static void readAll(DatabaseManager databaseManager, Technician technician) {
        try {
            System.out.println("Reading all tenants...");
            technician.logMeterReadings(databaseManager.getAllTenants());
            System.out.println("All tenants read complete!");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        }
    }
}