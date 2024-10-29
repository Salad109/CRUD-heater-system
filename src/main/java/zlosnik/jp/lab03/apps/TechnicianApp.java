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

        char choice;
        do {
            int n = 0;
            System.out.println("Technician app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read orders");
            System.out.println(n++ + ". Delete oldest order");
            System.out.println(n++ + ". Read a specific tenant");
            System.out.println(n++ + ". Read a specific street");
            System.out.println(n + ". Read all tenants");
            choice = scanner.nextLine().charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    readOrders(technician);
                    break;
                case '2':
                    deleteOrder(technician);
                    break;
                case '3':
                    readID(scanner, databaseManager, technician);
                    break;
                case '4':
                    readStreet(scanner, databaseManager, technician);
                    break;
                case '5':
                    readAll(databaseManager, technician);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }

    private static void readOrders(Technician technician) {
        technician.readOrders();
    }

    private static void deleteOrder(Technician technician) {
        System.out.println("Deleting oldest order");
        technician.deleteFirstOrder();
        System.out.println("Deleted oldest order");
    }

    private static void readID(Scanner scanner, DatabaseManager databaseManager, Technician technician) {
        System.out.println("Provide tenant ID:");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Reading tenant...");
            Tenant tenant = databaseManager.getTenantByID(id);
            if (tenant != null) {
                technician.getMeterReading(tenant);
                System.out.println("Tenant read complete.");
            } else {
                System.out.println("Tenant not found.");
            }
        } catch (InputMismatchException e) {
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
            if (tenantList != null) {
                technician.getMeterReadings(tenantList);
                System.out.println("Street read complete.");
            } else {
                System.out.println("Street not found.");
            }
        } catch (StreetNotFoundException e) {
            System.out.println("Street not found.");
        }
    }

    private static void readAll(DatabaseManager databaseManager, Technician technician) {
        System.out.println("Reading all tenants...");
        technician.getMeterReadings(databaseManager.getAllTenants());
        System.out.println("All tenants read complete.");
    }
}