package src;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import src.Log.TransactionType;

public class StadiumSeats {
    static HashMap<String, Client> clientDatabase = new HashMap<>();

    static SeatSection fieldSeats = new SeatSection("Field", "$300.00", 500);
    static SeatSection mainSeats = new SeatSection("Main", "$120.00", 1000);
    static SeatSection grandStandSeats = new SeatSection("GrandStand", "$45.00", 2000);

    static LinkedList<Log> history = new LinkedList<>();

    static Scanner scanner;

    private static void printMenuHeader(String head) {
        String outline = "---------------------------------------";
        System.out.println("/" + outline + head + outline + "/" + "\n");
    }

    public static void menu() {
        Client currClient = getClientInformation();

        System.out.println("\nWelcome, " + currClient +  "!");

        while (true) {
            printMenuHeader("Menu");

            System.out.println(
                "Enter the appropriate number for the action you want to perform:\n" +
                "(0) Exit\n" +
                "(1) Purchase Seat\n" +
                "(2) View Available Seats\n" +
                "(3) Cancel Reservation\n" +
                "(4) Undo\n" +
                "(5) Change Account\n" +
                "(6) View logs\n"
            );
            String selection = scanner.nextLine();

            if (selection == null || selection.trim().isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }

            switch (selection) {
                case "0" -> { return; }
                case "1" -> {
                    printMenuHeader("Purchase Seat");
                    purchaseSeat(currClient);
                }
                case "2" -> {
                    printMenuHeader("View Available Seats");
                    displayAvailability();
                }
                case "3" -> {
                    printMenuHeader("Cancel Reservation");
                    cancelReservation(currClient);
                }
                case "4" -> {
                    printMenuHeader("Undo");
                    currClient.undo();
                }
                case "5" -> {
                    printMenuHeader("Change Account");
                    currClient = getClientInformation();
                }
                case "6" -> {
                    printMenuHeader("Logs");
                    showLogs();
                }
                default -> { System.out.println("Please enter a valid option."); }
            }

            System.out.println("Press enter to continue...");
            scanner.nextLine();
        }  

    }

    public static boolean askYesNo(String question) {
        while (true) {
            System.out.println(question);
            switch (scanner.nextLine().toLowerCase()) {
                case "yes", "y" -> { return true; }
                case "no", "n" -> { return false; }
                default -> System.out.println("Please enter a valid option. (Y/N)");
            }
        }
    }

    public static void showLogs() {
        int logCount = history.size();
        int showLogs = 0;
        if (logCount > 10) {
            System.out.print("How many logs would you like to see? (0-" + logCount + "): ");
            try { showLogs = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            while (showLogs < 1 || showLogs > logCount) {
                System.out.print("Try again, how many logs would you like to see? (0-" + logCount + "): ");
                try { showLogs = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            }
        } else {
            showLogs = logCount;
        }

        Iterator<Log> iter = history.descendingIterator();
        for (int i = 0; i < showLogs; i++) {
            System.out.println(iter.next());
        }
    }

    public static void displayAvailability() {
        fieldSeats.displayAvailability();
        mainSeats.displayAvailability();
        grandStandSeats.displayAvailability();
    }

    public static SeatSection getSection(String message) {
        System.out.println(message);
        System.out.println("(0) Cancel");
        System.out.println("(1) " + fieldSeats);
        System.out.println("(2) " + mainSeats);
        System.out.println("(3) " + grandStandSeats);

        SeatSection selectedSection = null;
        boolean selecting = true;
        while (selecting) {
            switch (scanner.nextLine()) {
                case "0" -> { return null; }
                case "1" -> { selectedSection = fieldSeats; selecting = false; }
                case "2" -> { selectedSection = mainSeats; selecting = false; }
                case "3" -> { selectedSection = grandStandSeats; selecting = false; }
                default -> { System.out.println("Please enter a valid option."); continue; }
            }
        }

        return selectedSection;
    }

    public static void cancelReservation(Client client) {
        SeatSection section = getSection("Enter the section of the seat you'd like to cancel:");
        if (section == null) return;
        section.cancelReservation(scanner, client);
    }

    public static void purchaseSeat(Client client) {
        SeatSection section = getSection("Enter the section of the seat you'd like to reserve:");
        if (section == null) return;
        section.reserveSeat(scanner, client);
    }

    public static Client getClientInformation() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email addres: ");
        String email = scanner.nextLine();

        System.out.print("Phone number: ");
        String number = scanner.nextLine();

        if (clientDatabase.containsKey(name)) {
            // Fetch and update the pre-existing client object
            Client c = clientDatabase.get(name);
            c.email = email;
            c.number = number;
            return c;
        } else {
            // Create a new client object (and store it in the database)
            return new Client(name, email, number);
        }


    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        menu();
        scanner.close();
    }

}
