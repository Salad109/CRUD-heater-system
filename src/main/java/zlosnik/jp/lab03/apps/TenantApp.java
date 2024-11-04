package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Tenant;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TenantApp {
    public static void main(String[] args) {
        System.out.println("I'm a tenant!");
        Tenant tenant = null;
        Scanner scanner = new Scanner(System.in);

        do {
            DatabaseManager.readTenants();
            System.out.println("Which tenant are you? Provide your ID:");
            try {
                int id = Integer.parseInt(scanner.nextLine());
                scanner.nextLine();
                System.out.println("Reading tenant...");
                tenant = DatabaseManager.getTenantByID(id);
                System.out.println("Tenant read complete.");
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        } while (tenant == null);

        String choice;
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
                    scanner.close();
                    break;
                case "1":
                    print(tenant);
                    break;
                case "2":
                    generateHeat(tenant);
                    break;
                case "3":
                    checkBill(tenant);
                    break;
                case "4":
                    payBill(tenant, scanner);
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        } while (!choice.equals("0"));
    }

    private static void print(Tenant tenant) {
        System.out.println(tenant);
    }

    private static void generateHeat(Tenant tenant) {
        System.out.println("Generating heat...");
        tenant.generateHeat();
        System.out.println("Heat generated!");
    }


    private static void checkBill(Tenant tenant) {
        double bill = tenant.getBill();
        System.out.println("Your bill is " + bill);
    }

    private static void payBill(Tenant tenant, Scanner scanner) {
        System.out.println("How much would you like to pay off?");
        double payAmount;
        try {
            String line = scanner.nextLine();
            payAmount = Double.parseDouble(line);
            if (payAmount < 0) {
                System.out.println("Payment amount must be greater than 0.");
                return;
            }
            tenant.payBill(payAmount);
            System.out.println("Bill paid successfully!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}