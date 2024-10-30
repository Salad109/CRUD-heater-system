package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Manager;

import java.util.InputMismatchException;
import java.util.Scanner;


public class ManagerApp {
    public static void main(String[] args) {
        // TODO RENT
        // TODO Add and delete tenants across ALL(!!!) of the .txt files
        Manager manager = new Manager();
        DatabaseManager databaseManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);

        char choice;
        do {
            int n = 0;
            System.out.println("Manager app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Update tenants");
            System.out.println(n++ + ". Print tenants");
            System.out.println(n++ + ". Issue order to read a specific tenant");
            System.out.println(n++ + ". Issue order to read a specific street");
            System.out.println(n + ". Issue order to read all tenants");
            String input = scanner.nextLine();
            choice = input.charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    System.out.println("Updating tenants...");
                    databaseManager.updateTenants();
                    System.out.println("Tenants updated!");
                    break;
                case '2':
                    System.out.println("Printing tenants...");
                    databaseManager.printTenants();
                    break;
                case '3':
                    System.out.println("Issuing order to read a specific tenant. Provide tenant ID:");
                    try {
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        manager.issueOrder("Read tenant, " + id);
                        System.out.println("Order Issued!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input.");
                    }
                    break;
                case '4':
                    System.out.println("Issuing order to read a specific street. Provide street:");
                    String street = scanner.nextLine();
                    manager.issueOrder("Read, street, " + street);
                    System.out.println("Order Issued!");
                    break;
                case '5':
                    System.out.println("Issuing order to read all tenants...");
                    manager.issueOrder("Read, all");
                    System.out.println("Order Issued!");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}
