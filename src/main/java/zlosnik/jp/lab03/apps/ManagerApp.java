package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Manager;
import zlosnik.jp.lab03.actors.TenantNotFoundException;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class ManagerApp {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);

        String choice;
        do {
            int n = 0;
            System.out.println("Manager app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Create a new tenant");
            System.out.println(n++ + ". Read tenants");
            System.out.println(n++ + ". Update tenants");
            System.out.println(n++ + ". Delete tenant");
            System.out.println(n++ + ". Issue order to read a specific tenant");
            System.out.println(n++ + ". Issue order to read a specific street");
            System.out.println(n++ + ". Issue order to read all tenants");
            System.out.println(n + ". Bill a tenant");
            choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    scanner.close();
                    break;
                case "1":
                    createTenant(scanner);
                    break;
                case "2":
                    readTenants();
                    break;
                case "3":
                    updateTenants();
                    break;
                case "4":
                    deleteTenant(scanner);
                    break;
                case "5":
                    issueIdRead(scanner, manager);
                    break;
                case "6":
                    issueStreetRead(scanner, manager);
                    break;
                case "7":
                    issueAllRead(manager);
                    break;
                case "8":
                    bill(scanner, manager);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!choice.equals("0"));
    }

    private static void createTenant(Scanner scanner) {
        System.out.println("Provide street of the new tenant:");
        String street = scanner.nextLine();

        List<Double> heaterSizes = new ArrayList<>();
        double input = -1;
        do {
            try {
                System.out.println("Provide next heater size(input 0 to stop adding).");
                String line = scanner.nextLine();
                input = Double.parseDouble(line);
                if (input != 0) heaterSizes.add(input);
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        } while (input != 0);
        try {
            DatabaseManager.createTenant(street, heaterSizes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readTenants() {
        DatabaseManager.readTenants();
    }

    private static void updateTenants() {
        System.out.println("Updating tenants...");
        DatabaseManager.updateTenants();
        System.out.println("Tenants updated!");
    }

    private static void deleteTenant(Scanner scanner) {
        System.out.println("Provide tenant ID to remove:");
        try {
            String input = scanner.nextLine();
            int id = Integer.parseInt(input);
            DatabaseManager.deleteTenant(id);
            System.out.println("Tenant removed from database!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found!");
        }
    }

    private static void issueIdRead(Scanner scanner, Manager manager) {
        System.out.println("Issuing order to read a specific tenant. Provide tenant ID:");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            manager.issueOrder(order("ID", Integer.toString(id)));
            System.out.println("Order Issued!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void issueStreetRead(Scanner scanner, Manager manager) {
        System.out.println("Issuing order to read a specific street. Provide street:");
        String street = scanner.nextLine();
        if (street.isBlank()) {
            System.out.println("Invalid input.");
            return;
        }
        manager.issueOrder(order("Street", street));
        System.out.println("Order Issued!");
    }

    private static void issueAllRead(Manager manager) {
        System.out.println("Issuing order to read all tenants...");
        manager.issueOrder(order("All", ""));
        System.out.println("Order Issued!");
    }

    private static String order(String type, String target) {
        return "Read, " + type + ", " + target + ", " + Instant.now();
    }

    private static void bill(Scanner scanner, Manager manager) {
        System.out.println("Provide tenant ID to be billed:");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            double bill = manager.billTenant(id);
            System.out.println("Tenant billed for " + bill + "!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found!");
        }
    }
}
