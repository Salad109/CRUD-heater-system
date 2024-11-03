package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Tenant;
import zlosnik.jp.lab03.actors.TenantNotFoundException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TenantApp {
    public static void main(String[] args) {
        System.out.println("I'm a tenant!");
        Tenant tenant = null;

        do {
            DatabaseManager.readTenants();
            System.out.println("Which tenant are you? Provide your ID:");
            try {
                Scanner scanner = new Scanner(System.in);
                int tenantID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Reading tenant...");
                tenant = DatabaseManager.getTenantByID(tenantID);
                System.out.println("Tenant read complete.");
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
            System.out.println(n++ + ". Generate heat");
            System.out.println(n++ + ". Check my bill");
            System.out.println(n + ". Pay my bill");
            choice = scanner.nextLine();

            switch (choice) {
                case "0":
                    break;
                case "1":
                    System.out.println(tenant);
                    break;
                case "2":
                    generateHeat(tenant);
                    break;
                case "3":
                    checkBill();
                    break;
                case "4":
                    payBill();
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        } while (!choice.equals("0"));
    }

    private static void generateHeat(Tenant tenant) {
        System.out.println("Generating heat...");
        try {
            tenant.generateHeat();
            System.out.println("Heat generated!");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found in database.");
        }
    }

    private static void checkBill() {
    }

    private static void payBill() {
    }
}