package zlosnik.jp.lab03.apps;

import zlosnik.jp.lab03.actors.DatabaseManager;
import zlosnik.jp.lab03.actors.Manager;
import zlosnik.jp.lab03.actors.TenantNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class ManagerApp {
    public static void main(String[] args) {
        Manager manager = new Manager();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        do {
            int n = 0;
            System.out.println("Manager app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Create a new tenant");
            System.out.println(n++ + ". Read tenants");
            System.out.println(n++ + ". Refresh tenants");
            System.out.println(n++ + ". Delete tenant");
            System.out.println(n++ + ". Issue order to read a specific tenant");
            System.out.println(n++ + ". Issue order to read a specific street");
            System.out.println(n++ + ". Issue order to read all tenants");
            System.out.println(n++ + ". Bill a tenant");
            System.out.println(n + ". View tenant's billing history");

            try {
                System.out.print("Enter choice: ");
                choice = reader.readLine();

                switch (choice) {
                    case "0":
                        reader.close();
                        break;
                    case "1":
                        createTenant(reader);
                        break;
                    case "2":
                        readTenants();
                        break;
                    case "3":
                        refreshTenants();
                        break;
                    case "4":
                        deleteTenant(reader);
                        break;
                    case "5":
                        issueIdRead(reader, manager);
                        break;
                    case "6":
                        issueStreetRead(reader, manager);
                        break;
                    case "7":
                        issueAllRead(manager);
                        break;
                    case "8":
                        bill(reader, manager);
                        break;
                    case "9":
                        viewBillingHistory(reader);
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Input exception.");
            }
        } while (!choice.equals("0"));
    }

    private static void createTenant(BufferedReader reader) {
        List<Double> heaterSizes = new ArrayList<>();
        try {
            System.out.print("Provide street of the new tenant: ");
            String street = reader.readLine();
            System.out.println("Provide next heater size(input 0 to stop adding).");
            while (true) {
                System.out.print("Heater size: ");
                double input = Double.parseDouble(reader.readLine());
                if (input == 0) break;
                heaterSizes.add(input);
            }
            DatabaseManager.createTenant(street, heaterSizes);
            System.out.println("Tenant created!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. " + e.getMessage());
        }
    }

    private static void readTenants() {
        DatabaseManager.readTenants();
    }

    private static void refreshTenants() {
        System.out.println("Updating tenants...");
        DatabaseManager.refreshTenants();
        System.out.println("Tenants refreshed!");
    }

    private static void deleteTenant(BufferedReader reader) {
        try {
            System.out.print("Provide tenant ID to remove: ");
            int id = Integer.parseInt(reader.readLine());
            DatabaseManager.deleteTenant(id);
            System.out.println("Tenant removed from database!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found!");
        }
    }

    private static void issueIdRead(BufferedReader reader, Manager manager) {
        try {
            System.out.print("Issuing order to read a specific tenant. Provide tenant ID: ");
            int id = Integer.parseInt(reader.readLine());
            manager.issueOrder(order("ID", Integer.toString(id)));
            System.out.println("Order Issued!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void issueStreetRead(BufferedReader reader, Manager manager) {
        try {
            System.out.print("Issuing order to read a specific street. Provide street: ");
            String street = reader.readLine();
            manager.issueOrder(order("Street", street));
            System.out.println("Order Issued!");
        } catch (IOException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void issueAllRead(Manager manager) {
        System.out.println("Issuing order to read all tenants...");
        try {
            manager.issueOrder(order("All", ""));
        } catch (IOException e) {
            System.out.println("Failed to issue order.");
        }
        System.out.println("Order Issued!");
    }

    private static String order(String type, String target) {
        return "Read, " + type + ", " + target + ", " + Instant.now();
    }

    private static void bill(BufferedReader reader, Manager manager) {
        try {
            System.out.print("Provide tenant ID to be billed: ");
            int id = Integer.parseInt(reader.readLine());
            double bill = manager.billTenant(id);
            System.out.println("Tenant billed for " + bill + "!");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewBillingHistory(BufferedReader reader) {
        try {
            System.out.print("Provide tenant ID to view their history: ");
            int id = Integer.parseInt(reader.readLine());
            List<String> lines = DatabaseManager.readFile(DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("payment-logs.txt"));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
