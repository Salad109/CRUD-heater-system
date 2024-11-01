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
            choice = scanner.nextLine();
            switch (choice) {
                case "0":
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
                    deleteTenant();
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
        double input;
        try {
            do {
                System.out.println("Provide next heater size(input 0 to stop adding).");
                String line = scanner.nextLine();
                input = Double.parseDouble(line);
                if (input != 0) heaterSizes.add(input);
            } while (input != 0);
            DatabaseManager.createTenant(street, heaterSizes);
        } catch (Exception e) {
            System.out.println("Invalid input.");
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

    private static void deleteTenant() {
    }

    private static void issueIdRead(Scanner scanner, Manager manager) {
        System.out.println("Issuing order to read a specific tenant. Provide tenant ID:");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            manager.issueOrder("Read, ID, " + id);
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

}
