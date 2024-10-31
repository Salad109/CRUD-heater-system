package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Tenant;
import zlosnik.jp.lab03.actors.TenantNotFoundException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TenantApp {
    public static void main(String[] args) {
        System.out.println("I'm a tenant!");
        DatabaseManager databaseManager = new DatabaseManager();
        Tenant tenant = null;

        do {
            databaseManager.printTenants();
            System.out.println("Which tenant are you? Provide your ID:");
            try {
                Scanner scanner = new Scanner(System.in);
                int tenantID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Reading tenant...");
                tenant = databaseManager.getTenantByID(tenantID);
                if (tenant != null) {
                    System.out.println("Tenant read complete.");
                } else {
                    System.out.println("Tenant not found.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
            } catch (TenantNotFoundException e) {
                System.out.println("Tenant not found.");
            }
        } while (tenant == null);

        String choice;
        Scanner scanner = new Scanner(System.in);
        do {
            int n = 0;
            System.out.println("Tenant App:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read my data");
            System.out.println(n + ". Generate heat");
            choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    break;
                case "1":
                    System.out.println(tenant);
                    break;
                case "2":
                    System.out.println("Generating heat...");
                    try {
                        tenant.generateHeat();
                        System.out.println("Heat generated!");
                    } catch (TenantNotFoundException e) {
                        System.out.println("Tenant not found in database.");
                    }
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        } while (!choice.equals("0"));
    }
}