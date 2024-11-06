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
        String choice = null;
        do {
            int n = 0;
            System.out.println("Manager app:");
            System.out.println(n++ + ". Exit");
            System.out.println(n++ + ". Create a new tenant");
            System.out.println(n++ + ". Read tenants");
            System.out.println(n++ + ". Modify tenant");
            System.out.println(n++ + ". Delete tenant");
            System.out.println(n++ + ". Issue order to read a specific tenant");
            System.out.println(n++ + ". Issue order to read a specific street");
            System.out.println(n++ + ". Issue order to read all tenants");
            System.out.println(n++ + ". Bill a tenant");
            System.out.println(n + ". View tenant's billing history");

            try {
                choice = readInput("Enter choice: ", reader);

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
                        modifyTenant(reader, manager);
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
            System.out.println("Provide street of the new tenant.");
            String street = readInput("Enter street name: ", reader);
            System.out.println("Provide next heater size(input 0 to stop adding).");
            while (true) {
                double input = Double.parseDouble(readInput("Enter heater size: ", reader));
                if (input == 0) break;
                heaterSizes.add(input);
            }
            DatabaseManager.createTenant(street, heaterSizes);
            System.out.println("Tenant created!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void readTenants() {
        DatabaseManager.readTenants();
    }

    private static void modifyTenant(BufferedReader reader, Manager manager) {
        loadTenant(reader, manager);
        String choice = getChoice(reader);

        switch (choice) {
            case "0":
                return;
            case "1":
                try {
                    System.out.println("Changing street name.");
                    String street = readInput("Enter new street name: ", reader);
                    manager.modifyStreet(street);
                    System.out.println("Street renamed!");
                } catch (TenantNotFoundException e) {
                    System.out.println("Tenant not found!");
                }
                break;
            case "2":
                List<Double> heaterSizes = new ArrayList<>();
                System.out.println("Provide next heater size(input 0 to stop adding).");
                while (true) {
                    double input = Double.parseDouble(readInput("Heater size: ", reader));
                    if (input == 0) break;
                    heaterSizes.add(input);
                }
                try {
                    manager.modifyHeaters(heaterSizes);
                    System.out.println("Heaters changed!");
                } catch (TenantNotFoundException e) {
                    System.out.println("Tenant not found!");
                }
                break;
            case "3":
                try {
                    System.out.println("Wiping bill...");
                    manager.cleanBill();
                    System.out.println("Bill wiped!");
                } catch (TenantNotFoundException e) {
                    System.out.println("Tenant not found!");
                }
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private static void loadTenant(BufferedReader reader, Manager manager) {
        try {
            System.out.println("Modifying tenant.");
            int id = Integer.parseInt(readInput("Provide tenant ID to modify: ", reader));
            manager.setTenant(id);
            System.out.println(manager.getTenant());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found!");
        }
    }

    private static String getChoice(BufferedReader reader) {
        System.out.println("What would you like to do to the tenant?");
        int n = 0;
        System.out.println(n++ + ". Exit");
        System.out.println(n++ + ". Change street name");
        System.out.println(n++ + ". Change heaters");
        System.out.println(n + ". Clear bill");

        return readInput("Enter choice: ", reader);
    }

    private static void deleteTenant(BufferedReader reader) {
        try {
            System.out.println("Removing tenant.");
            int id = Integer.parseInt(readInput("Enter tenant ID to remove: ", reader));
            DatabaseManager.deleteTenant(id);
            System.out.println("Tenant removed from database!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found!");
        }
    }

    private static void issueIdRead(BufferedReader reader, Manager manager) {
        try {
            System.out.println("Issuing order to read a specific tenant. Provide tenant ID.");
            int id = Integer.parseInt(readInput("Enter tenant ID: ", reader));
            manager.issueOrder(order("ID", Integer.toString(id)));
            System.out.println("Order Issued!");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void issueStreetRead(BufferedReader reader, Manager manager) {
        try {
            System.out.println("Issuing order to read a specific street. Provide street name.");
            String street = readInput("Enter street name: ", reader);
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
            System.out.println("Billing tenant.");
            int id = Integer.parseInt(readInput("Enter tenant ID to be billed: ", reader));
            double bill = manager.billTenant(id);
            System.out.println("Tenant billed for " + bill + "!");
        } catch (TenantNotFoundException e) {
            System.out.println("Tenant not found.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewBillingHistory(BufferedReader reader) {
        try {
            System.out.println("Checking billing history.");
            int id = Integer.parseInt(readInput("Enter tenant ID to view their history: ", reader));
            List<String> lines = DatabaseManager.readFile(DatabaseManager.TENANTS_DIRECTORY.resolve(Integer.toString(id)).resolve("payment-logs.txt"));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static String readInput(String prompt, BufferedReader reader) {
        System.out.print(prompt);
        String input = null;
        do {
            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Input error. Please try again.");
            }
        } while (input == null);
        return input;
    }
}
