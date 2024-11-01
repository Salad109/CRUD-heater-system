package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Manager;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class ManagerApp {
    public static void main(String[] args) {
        // TODO RENT
        // TODO Add and delete tenants across ALL(!!!) of the .txt files
        Manager manager = new Manager();
        DatabaseManager databaseManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);

        String choice;
        do {
            int n = 0;
            System.out.println("Manager app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Update tenants");
            System.out.println(n++ + ". Print tenants");
            System.out.println(n++ + ". Issue order to read a specific tenant");
            System.out.println(n++ + ". Issue order to read a specific street");
            System.out.println(n++ + ". Issue order to read all tenants");
            System.out.println(n + ". Create a new tenant");
            choice = scanner.nextLine();
            switch (choice) {
                case "0":
                    break;
                case "1":
                    updateTenants(databaseManager);
                    break;
                case "2":
                    printTenants(databaseManager);
                    break;
                case "3":
                    issueIdRead(scanner, manager);
                    break;
                case "4":
                    issueStreetRead(scanner, manager);
                    break;
                case "5":
                    issueAllRead(manager);
                    break;
                case "6":
                    addTenant(scanner, manager);
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (!choice.equals("0"));
    }

    private static void updateTenants(DatabaseManager databaseManager) {
        System.out.println("Updating tenants...");
        databaseManager.updateTenants();
        System.out.println("Tenants updated!");
    }

    private static void printTenants(DatabaseManager databaseManager) {
        databaseManager.printTenants();
    }

    private static void issueIdRead(Scanner scanner, Manager manager) {
        System.out.println("Issuing order to read a specific tenant. Provide tenant ID:");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            manager.issueOrder("Read tenant, " + id);
            System.out.println("Order Issued!");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void issueStreetRead(Scanner scanner, Manager manager) {
        System.out.println("Issuing order to read a specific street. Provide street:");
        String street = scanner.nextLine();
        manager.issueOrder("Read, street, " + street);
        System.out.println("Order Issued!");
    }

    private static void issueAllRead(Manager manager) {
        System.out.println("Issuing order to read all tenants...");
        manager.issueOrder("Read, all");
        System.out.println("Order Issued!");
    }

    private static void addTenant(Scanner scanner, Manager manager) {
        System.out.println("Provide street of the new tenant:");
        String street = scanner.nextLine();

        List<Double> heaterSizes = new ArrayList<>();
        double input;
        try {
            do {
                System.out.println("Provide next heater size(input 0 to stop adding).");
                String line = scanner.nextLine();
                input = Double.parseDouble(line);
                if (input != 0) heaterSizes.add(input);
            } while (input != 0);
            manager.addTenant(street, heaterSizes);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
        }
    }
}
