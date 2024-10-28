package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.manager.Manager;

import java.util.Scanner;


public class ManagerApp {
    public static void main(String[] args) {
        System.out.println("I'm a manager!");
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);
        char choice;
        do {
            System.out.println("Manager app:");
            System.out.println("0. Exit");
            System.out.println("1. Update tenants");
            System.out.println("2. Print tenants");
            String input = scanner.nextLine();
            choice = input.charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    manager.updateTenants();
                    System.out.println("Tenants updated!");
                    break;
                case '2':
                    manager.printTenants();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}
