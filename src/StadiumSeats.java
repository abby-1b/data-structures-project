package src;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Calls methods from other classes and provides some common methods (mostly for
 * user input). It handles the menu and its basic tasks, which themselves call
 * other functions.
 * 
 * It uses a HashMap for clientDatabase, which stores different clients along
 * with their histories. This means that although we have a global history, it's
 * only used as a log. Client undo is handled in the `Client` object itself,
 * completely independent of other clients. A hashmap is used because it can
 * quickly look up client names and return the corresponding client object.
 * 
 * A linked list is used to store the local history, which is only added to.
 */
public class StadiumSeats {
    static HashMap<String, Client> clientDatabase = new HashMap<>();

    static SeatSection fieldSeats = new SeatSection("Field", "$300.00", 500);
    static SeatSection mainSeats = new SeatSection("Main", "$120.00", 1000);
    static SeatSection grandStandSeats = new SeatSection("GrandStand", "$45.00", 2000);

    static LinkedList<Log> history = new LinkedList<>();

    // This scanner is both opened and closed in `main()`!
    static Scanner scanner;

    /**
     * Prints a menu header with the given text.
     * @param head The message printed in the middle of the header
     * Called multiple times within `menu()`.
     */
    private static void printMenuHeader(String head) {
        String outline = "---------------------------------------";
        System.out.println("/" + outline + head + outline + "/" + "\n");
    }

    /**
     * A main menu system where users interact with the application.
     * 
     * Calls other functions like `purchaseSeat(currClient)`,
     * `displayAvailability()`, `cancelReservation(currClient)`,
     * `Client.undo()`, `getClientInformation()`, and `showLogs()`.
     * 
     * Only called by `main()`.
     */
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

    /**
     * Prompts a yes/no question to the user
     * @param question The question to ask to the user
     * Called by `SeatSection.askForWaitlist()` and `Client.undo()`.
     */
    public static boolean askYesNo(String question) {
        while (true) {
            System.out.println(question + " (Y/N)");
            switch (scanner.nextLine().toLowerCase()) {
                case "yes", "y" -> { return true; }
                case "no", "n" -> { return false; }
                default -> System.out.println("Please enter a valid option.");
            }
        }
    }

    /**
     * Asks for a single character. If no character is supplied (enter is
     * pressed), returns a null character (0).
     * @return The given single character.
     * Only called by `Seat.pick()`.
     */
    public static char askForChar(Scanner scanner) {
        String l = scanner.nextLine();
        if (l.length() == 0) return '\0';
        else return l.charAt(0);
    }

    /**
     * Shows the global transaction history.
     * 
     * If there are more than 10 logs, the user is asked how many logs they want
     * to see. To do this, the list of logs is traversed backwards and put into
     * a stack, which is then popped from. This reverses and then un-reverses
     * the order of the requested logs, displaying them properly.
     * 
     * Called by `menu()`.
     */
    public static void showLogs() {
        int logCount = history.size();
        int showLogs = 0;
        if (logCount > 10) {
            // Too many logs, ask for an amount
            System.out.print("How many logs to display? (0-" + logCount + "): ");
            try { showLogs = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            while (showLogs < 1 || showLogs > logCount) {
                System.out.print("Try again, how many logs to display? (0-" + logCount + "): ");
                try { showLogs = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            }
        } else {
            // Just show all the logs
            showLogs = logCount;
        }

        Stack<Log> logs = new Stack<>();
        Iterator<Log> iter = history.descendingIterator();
        for (int i = 0; i < showLogs; i++) logs.add(iter.next());
        while (!logs.isEmpty()) System.out.println(logs.pop());
    }

    /**
     * Displays the avialable seats in each section.
     * Calls `SeatSection.displayAvailability()` on all sections.
     * Called within `menu()`.
     */
    public static void displayAvailability() {
        fieldSeats.displayAvailability();
        mainSeats.displayAvailability();
        grandStandSeats.displayAvailability();
    }

    /**
     * Asks the user to pick a section
     * @param message The message to print before picking a section
     * @return The picked section, or null if cancelled
     * 
     * This is called by `cancelReservation()` and `purchaseSeat()`.
     */
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

    /**
     * Makes a reservation for a client.
     * @param client The client to make a reservation for
     * Called by `menu()`.
     */
    public static void purchaseSeat(Client client) {
        SeatSection section = getSection("Enter the section of the seat to be to reserved:");
        if (section == null) return;
        section.reserveSeat(scanner, client);
    }

    /**
     * Cancels a reservation for a client.
     * @param client The client to cancel a reservation for
     * Called by `menu()`.
     */
    public static void cancelReservation(Client client) {
        SeatSection section = getSection("Enter the section of the seat to be to canceled:");
        if (section == null) return;
        section.cancelReservation(scanner, client);
    }

    /**
     * Asks for client information, using their name as a key.
     * If an existing name is provided, the email and phone number is changed to
     * fit the previously existing client. If no client existed, one is created
     * and added to the database.
     * @returns The requested client
     * Called by `menu()`.
     */
    public static Client getClientInformation() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        if (clientDatabase.containsKey(name)) {
            // Fetch the pre-existing client object
            Client c = clientDatabase.get(name);
            return c;
        } else {
            // Ask for the remaining information
            System.out.print("Email addres: ");
            String email = scanner.nextLine();

            System.out.print("Phone number: ");
            String number = scanner.nextLine();

            // Create a new client object (and store it in the database)
            return new Client(name, email, number);
        }
    }

    /**
     * Entry point for the program. Builds and destroys the scanner.
     * Calls the `menu()` function, which then handles all the requested user
     * operations and looping.
     */
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        menu();
        scanner.close();
    }

}
