// package src.main;
import java.util.Scanner;

// I think this would be the stadium class, not sure though //
public class StadiumSeats {
    // TODO: put these in a set //
    static Seats FieldSeats = new Seats("Field", 300, 500);
    static Seats MainSeats = new Seats("Main", 120, 1000);
    static Seats GrandStandSeats = new Seats("GrandStand", 45, 2000);

    public static void menu() {
        // TODO: add the menu options //
    }

    public static void displayAvailability() {
        // TODO: display the capacity of the seats //
    }

    public static Client clientInformation() {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = s.nextLine();

        System.out.println();

        System.out.print("Enter your email addres: ");
        String email = s.nextLine();

        System.out.println();

        System.out.print("Enter your phone number: ");
        String number = s.nextLine();

        System.out.println();

        // HashMap<String, String> clientInformation = new HashMap<>();
        // clientInformation.put("Name", name);
        // clientInformation.put("Email", email);
        // clientInformation.put("Phone", number);

        Client c = new Client(name, email, number);

        s.close();

        return c;
    }

    static boolean purchaseConfirmation(Seats seat) {
        Scanner s = new Scanner(System.in);
        String confirmation = "";

        System.out.println("Would you like to proceed with your purchase of: ");
        System.out.println("Level: " + seat.level + ", for " + seat.cost + "$" + " (Y/N)");
        
        while (confirmation.equals("")) {
            confirmation = s.nextLine();

            switch (confirmation.toLowerCase()) {
                case "y":
                    return true;

                case "n":
                    return false;

                default:
                    System.out.println("Please enter a valid option. (Y/N)");
                    confirmation = "";
                    break;
            }
        }

        return false;
    }

    public static void purchaseSeat() {
        Scanner s = new Scanner(System.in);
        String seatToPurchase = "";
        
        System.out.println("Which seat would you like to purchase?");
        System.out.println("Enter the number for the seat you want to purchase (e.g. 1 for a Field seat) or enter 0 to exit.");

        System.out.println();

        System.out.print("(1) ");
        FieldSeats.printData();

        System.out.print("(2) ");
        MainSeats.printData();

        System.out.print("(3) ");
        GrandStandSeats.printData();


        while (seatToPurchase.equals("")) {
            seatToPurchase = s.nextLine();
            boolean purchase;

            switch (seatToPurchase) {
                case "1":
                    purchase = purchaseConfirmation(FieldSeats);
                    if (purchase && FieldSeats.capacity > 0) {
                        FieldSeats.setCapacity(FieldSeats.capacity - 1);
                        System.out.println("You have successfully purchased a Field seat.");
                        // TODO: add the log of the purchase //
                    } else if (purchase && FieldSeats.capacity <= 0) {
                        System.out.println("Sorry, the seat you are trying to purchase is unavailable.");
                        return;
                    } else { return; }

                    break;

                case "2":
                    purchase = purchaseConfirmation(MainSeats);
                    if (purchase && MainSeats.capacity > 0) {
                        MainSeats.setCapacity(MainSeats.capacity - 1);
                        System.out.println("You have successfully purchased a Main seat.");
                        // TODO: add the log of the purchase //
                    } else if (purchase && MainSeats.capacity <= 0) {
                        System.out.println("Sorry, the seat you are trying to purchase is unavailable.");
                        return;
                    } else { return; }

                    break;

                case "3":
                    purchase = purchaseConfirmation(GrandStandSeats);
                    if (purchase && GrandStandSeats.capacity > 0) {
                        GrandStandSeats.setCapacity(GrandStandSeats.capacity - 1);
                        System.out.println("You have successfully purchased a Grandstand seat.");
                        // TODO: add the log of the purchase //
                    } else if (purchase && GrandStandSeats.capacity <= 0) {
                        System.out.println("Sorry, the seat you are trying to purchase is unavailable.");
                        return;
                    } else { return; }

                    break;

                case "0":
                    return;

                default:
                    System.out.println("Please enter a valid option.");
                    seatToPurchase = "";
                    break;
            }
        }

        s.close();

    }

    public static void main(String[] args) {
        // Tests //
        // clientInformation().printData(); // Verified (AC)
        // purchaseConfirmation(MainSeats); // Verified (AC)
        // purchaseSeat(); // Verified (AC)

        System.out.println("it works i guess..");
    }

}
