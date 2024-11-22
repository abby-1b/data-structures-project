package src;
import java.util.HashMap;
import java.util.Scanner;

public class StadiumSeats {
    static SeatSection fieldSeats = new SeatSection("Field", 300, 500);
    static SeatSection mainSeats = new SeatSection("Main", 120, 1000);
    static SeatSection grandStandSeats = new SeatSection("GrandStand", 45, 2000);

    static HashMap<Client, Seat> takenSeats = new HashMap<>();

    public static void setupSeats() {
        //TODO: create seats
    }

    public static void menu() {
        // TODO: add the menu options
    }

    public static void displayAvailability() {
        // TODO: display the capacity of the seats
    }

    public static Client getClientInformation() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = s.nextLine();

        System.out.print("Enter your email addres: ");
        String email = s.nextLine();

        System.out.print("Enter your phone number: ");
        String number = s.nextLine();

        Client c = new Client(name, email, number);

        s.close();

        return c;
    }

    static Seat pickSeat(String section) {
        // This should have a try I think, and check the range of the inputs
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the row of your seat:");
        int row = s.nextInt();

        System.out.print("Enter the number of your seat:");
        int number = s.nextInt();

        s.close();

        return new Seat();
    }

    static boolean purchaseConfirmation(Seat seat) {
        System.out.println("Would you like to proceed with your purchase of: ");
        System.out.println(seat + " (Y/N)");
        
        Scanner s = new Scanner(System.in);
        while (true) {
            switch (s.nextLine().toLowerCase()) {
                case "yes", "y" -> { s.close(); return true ; }
                case "no" , "n" -> { s.close(); return false; }
                default -> System.out.println("Please enter a valid option. (Y/N)");
            }
        }
    }

    public static void purchaseSeat() {
        Scanner s = new Scanner(System.in);

        System.out.println("Which seat would you like to purchase?");
        System.out.println("Enter the number for the seat you want to purchase (e.g. 1 for a Field seat) or enter 0 to exit.");

        System.out.println();

        System.out.print("(1) " + fieldSeats);
        System.out.print("(2) " + mainSeats);
        System.out.print("(3) " + grandStandSeats);

        SeatSection selectedSection = null;
        boolean selecting = true;
        while (selecting) {
            switch (s.nextLine()) {
                case "0" -> { selecting = false; }
                case "1" -> { selectedSection = fieldSeats; selecting = false; }
                case "2" -> { selectedSection = mainSeats; selecting = false; }
                case "3" -> { selectedSection = grandStandSeats; selecting = false; }
                default -> { System.out.println("Please enter a valid option."); continue; }
            }
        }

        if (!purchaseConfirmation(new Seat())) return;
        if (selectedSection.getCapacity() > 0) {
            selectedSection.reserveSeat(0);
            System.out.println("You have successfully purchased a Field seat.");
            // TODO: add the log of the purchase

        } else if (selectedSection.getCapacity() <= 0) {
            System.out.println("Sorry, the seat you are trying to purchase is unavailable.");
            return;
        }
    }

    public static void main(String[] args) {
        // Tests
        // clientInformation().printData();
        // purchaseSeat();
        // pickSeat("Main");

        Seat s = new Seat(fieldSeats, 99);
        System.out.println(s);
    }

}
