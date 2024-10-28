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
            System.out.println("7. Issue order to read a specific tenant");
            System.out.println("8. Issue order to read a specific street");
            System.out.println("9. Issue order to read all tenants");
            String input = scanner.nextLine();
            choice = input.charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    System.out.println("Updating tenants...");
                    manager.updateTenants();
                    System.out.println("Tenants updated!");
                    break;
                case '2':
                    System.out.println("Printing tenants...");
                    manager.printTenants();
                    break;
                case '3':
                    System.out.println("Generating heat...");
                    for (Tenant tenant : manager.getAllTenants()) {
                        tenant.generateHeat();
                    }
                    break;
                case '4':
                    System.out.println("Provide tenant ID:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Reading tenant...");
                    technician.getMeterReading(manager.getTenantByID(id));
                    break;
                case '5':
                    System.out.println("Provide street:");
                    String street = scanner.next();
                    System.out.println("Reading street...");
                    technician.getMeterReadings(manager.getTenantsByStreet(street));
                    break;
                case '6':
                    System.out.println("Reading all tenants...");
                    technician.getMeterReadings(manager.getAllTenants());
                    break;
                case '7':
                    System.out.println("Issuing order to read a specific tenant. Provide tenant ID:");
                    int id2 = scanner.nextInt();
                    scanner.nextLine();
                    manager.issueOrder("Read tenant ID " + id2);
                    System.out.println("Order Issued!");
                    break;
                    case '8':
                    System.out.println("Issuing order to read a specific street. Provide street:");
                    String street2 = scanner.nextLine();
                    scanner.nextLine();
                    manager.issueOrder("Read street " + street2);
                    System.out.println("Order Issued!");
                    break;
                case '9':
                    System.out.println("Issuing order to read all tenants...");
                    manager.issueOrder("Read all");
                    System.out.println("Order Issued!");
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}
