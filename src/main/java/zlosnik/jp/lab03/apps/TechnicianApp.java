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
            String input = scanner.nextLine();
            choice = input.charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    technician.readOrders();
                    break;
                case '2':
                    System.out.println("Deleting oldest order");
                    technician.deleteFirstOrder();
                    System.out.println("Deleted oldest order");
                    break;
                case '3':
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
                    break;
                case '4':
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
                    }catch (StreetNotFoundException e){
                        System.out.println("Street not found.");
                    }
                    break;
                case '5':
                    System.out.println("Reading all tenants...");
                    technician.getMeterReadings(databaseManager.getAllTenants());
                    System.out.println("All tenants read complete.");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}