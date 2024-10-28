package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.manager.Manager;
import zlosnik.jp.lab03.technician.Technician;
import zlosnik.jp.lab03.tenant.Tenant;

import java.util.Scanner;


public class ManagerApp {
    public static void main(String[] args) {
        System.out.println("I'm a manager!");
        Manager manager = new Manager();
        Scanner scanner = new Scanner(System.in);

        Technician technician = new Technician();
        char choice;
        do {
            System.out.println("Manager app:");
            System.out.println("0. Exit");
            System.out.println("1. Update tenants");
            System.out.println("2. Print tenants");
            System.out.println("3. Generate heat");
            System.out.println("4. Read a specific tenant");
            System.out.println("5. Read a specific street");
            System.out.println("6. Read all tenants");
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
                case '3':
                    for (Tenant tenant : manager.getAllTenants()) {
                        tenant.generateHeat();
                    }
                    break;
                case '4':
                    System.out.println("Provide tenant ID:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    technician.getMeterReading(manager.getTenantByID(id));
                    break;
                case '5':
                    System.out.println("Provide street:");
                    String street = scanner.nextLine().trim();
                    technician.getMeterReadings(manager.getTenantsByStreet(street));
                    break;
                case '6':
                    System.out.println("Reading meters for all tenants...");
                    technician.getMeterReadings(manager.getAllTenants());
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}
