package src;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class StadiumSeats {
    static HashMap<String, Client> clientDatabase = new HashMap<>();

    static SeatSection fieldSeats = new SeatSection("Field", "$300.00", 500);
    static SeatSection mainSeats = new SeatSection("Main", "$120.00", 1000);
    static SeatSection grandStandSeats = new SeatSection("GrandStand", "$45.00", 2000);

    static LinkedList<Log> history = new LinkedList<>();
    static Stack<Log> clientHistory = new Stack<>();

    static Scanner scanner;

    public static void undoPurchase() {
        //TODO: undo most recent purchase for the current client
    }

    public static void menu() {
        Client currClient = getClientInformation();

        String outline = "---------------------------------------";
        System.out.println("/" + outline + "Menu" + outline + "/" + "\n");
        System.out.println("Welcome, " + currClient +  "!");

        //boolean selecting = true; // just in case we want to keep selecting after done with an option
        while (true) {
            System.out.println(
                "\nEnter the appropriate number for the action you want to perform:" +
                "\n(0) Exit" +
                "\n(1) Purchase Seat" +
                "\n(2) View Available Seats" +
                "\n(3) Cancel Reservation" + 
                "\n(4) Undo Purchase" + 
                "\n(5) Change Account"
            );
            String selection = scanner.nextLine();

            if (selection == null || selection.trim().isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }

            switch (selection) {
                case "0" -> {
                    return;
                }
                case "1" -> {
                    System.out.println("/" + outline + "Purchase Seat" + outline + "/" + "\n");
                    purchaseSeat(currClient);
                }
                case "2" -> {
                    System.out.println("/" + outline + "View Available Seats" + outline + "/");
                    displayAvailability();
                }
                case "3" -> {
                    System.out.println("/" + outline + "Cancel Reservation" + outline + "/");
                    // TODO: Implement seat canceling
                }
                case "4" -> {
                    System.out.println("/" + outline + "Undo Purchase" + outline + "/");
                    // TODO: Implement undo function
                }
                case "5" -> {
                    System.out.println("/" + outline + "Change Account" + outline + "/");
                    currClient = getClientInformation();
                }
                default -> {
                    System.out.println("Please enter a valid option.");
                }
            }
        }  

    }

    public static void displayAvailability() {
        fieldSeats.displayAvailability();
        mainSeats.displayAvailability();
        grandStandSeats.displayAvailability();
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

    static boolean purchaseConfirmation(Seat seat) {
        System.out.println("Would you like to proceed with your purchase of: ");
        System.out.println(seat + " (Y/N)");

        while (true) {
            switch (scanner.nextLine().toLowerCase()) {
                case "yes", "y" -> { return true ; }
                case "no" , "n" -> { return false; }
                default -> System.out.println("Please enter a valid option. (Y/N)");
            }
        }
    }

    public static void purchaseSeat(Client client) {
        System.out.println("Which seat would you like to purchase?");
        System.out.println("Enter the number for the seat you want to purchase (e.g. 1 for a Field seat) or enter 0 to exit.\n");

        System.out.println("(0) Cancel");
        System.out.println("(1) " + fieldSeats);
        System.out.println("(2) " + mainSeats);
        System.out.println("(3) " + grandStandSeats);

        SeatSection selectedSection = null;
        boolean selecting = true;
        while (selecting) {
            switch (scanner.nextLine()) {
                case "0" -> { return; }
                case "1" -> { selectedSection = fieldSeats; selecting = false; }
                case "2" -> { selectedSection = mainSeats; selecting = false; }
                case "3" -> { selectedSection = grandStandSeats; selecting = false; }
                default -> { System.out.println("Please enter a valid option."); continue; }
            }
        }

        selectedSection.pickSeat(scanner, client);
    }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        menu();
        scanner.close();
    }

}
