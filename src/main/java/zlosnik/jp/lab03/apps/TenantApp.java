package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Tenant;
import zlosnik.jp.lab03.actors.TenantNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

public class TenantApp {
    public static void main(String[] args) {
        System.out.println("I'm a tenant!");
        Tenant tenant = null;
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));

        do {
            DatabaseManager.readTenants();
            System.out.println("Which tenant are you? Provide your ID:");
            try {
                int id = Integer.parseInt(scanner.readLine());
                System.out.println("Reading tenant...");
                tenant = DatabaseManager.getTenantByID(id);
                System.out.println("Tenant read complete.");
            } catch (InputMismatchException | NumberFormatException | IOException e) {
                System.out.println("Invalid input.");
            } catch (TenantNotFoundException e) {
                System.out.println("Tenant not found.");
            }
        } while (tenant == null);

        String choice = "0";
        do {
            int n = 0;
            System.out.println("Tenant App:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Read my data");
            System.out.println(n++ + ". Generate heat");
            System.out.println(n++ + ". Check my bill");
            System.out.println(n + ". Pay my bill");
            try {
                choice = scanner.readLine();

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
            } catch (IOException e) {
                System.out.println("Input exception.");
            }
        } while (!choice.equals("0"));
    }

    private static void print(Tenant tenant) {
        try {
            tenant = DatabaseManager.getTenantByID(tenant.getId());
            System.out.println(tenant);
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        }
    }

    private static void generateHeat(Tenant tenant) {
        System.out.println("Generating heat...");
        try {
            tenant.generateHeat();
            System.out.println("Heat generated!");
        } catch (IOException e) {
            System.out.println("Failed to generate heat.");
        }
    }


    private static void checkBill(Tenant tenant) {
        System.out.println("Checking bill...");
        try {
            double bill = tenant.getBill();
            System.out.println("Your bill is " + bill);
        } catch (IOException e) {
            System.out.println("Failed to get bill.");
        }
    }

    private static void payBill(Tenant tenant, BufferedReader scanner) {
        System.out.println("How much would you like to pay off?");
        double payAmount;
        try {
            String line = scanner.readLine();
            payAmount = Double.parseDouble(line);
            if (payAmount < 0) {
                System.out.println("Payment amount must be greater than 0.");
                return;
            }
            tenant.payBill(payAmount);
            System.out.println("Bill paid successfully!");
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (IOException e) {
            System.out.println("Failed to pay bill.");
        }
    }
}