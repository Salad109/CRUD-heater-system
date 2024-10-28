package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.technician.Technician;

import java.util.Scanner;

public class TechnicianApp {
    public static void main(String[] args) {
        Technician technician = new Technician();
        Scanner scanner = new Scanner(System.in);

        char choice;
        do {
            int n = 0;
            System.out.println("Technician app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read a specific tenant");
            System.out.println(n++ + ". Read a specific street");
            System.out.println(n + ". Read all tenants");
            String input = scanner.nextLine();
            choice = input.charAt(0);
            switch (choice) {
                case '0':
                    break;
                case '1':
                    System.out.println("Provide tenant ID:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Reading tenant...");
                    technician.getMeterReading(manager.getTenantByID(id));
                    break;
                case '2':
                    System.out.println("Provide street:");
                    String street = scanner.next();
                    System.out.println("Reading street...");
                    technician.getMeterReadings(manager.getTenantsByStreet(street));
                    break;
                case '3':
                    System.out.println("Reading all tenants...");
                    technician.getMeterReadings(manager.getAllTenants());
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != '0');
    }
}