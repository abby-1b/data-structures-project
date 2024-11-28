package src;

import java.util.HashSet;
import java.util.Scanner;

/**
 * A single seat, with its seat index and a way to display its prettified ID
 * (like `A12` for seat index `11`).
 * 
 * The seat class uses no convoluted data structures outside of ones we defined,
 * which are documented in their respective files.
 * 
 * Each seat stores a reference to its parent section, which remains static
 * throughout its lifetime. It also has a reference to the client that currently
 * owns this seat. When the seat is not reserved, the client is null.
 */
public class Seat {
    final SeatSection section;
    int number;

    /** The client this seat belongs to */
    Client client;

    public Seat(SeatSection section, int number) {
        this.section = section;
        this.number = number;
    }

    /**
     * Takes a seat, making it unavailable.
     * @param client The client that is taking this seat.
     * 
     * This function updates the section's waitlist, because when it's
     * taken it _could_ just be switching from one client to another. It also
     * modifies the parent section's available/taken seats.
     * 
     * It's only called from the `Client.take()` method.
     */
    public void take(Client client) {
        this.client = client;
        this.section.availableSeats.remove(this);
        if (!this.section.takenSeats.containsKey(client)) {
            this.section.takenSeats.put(client, new HashSet<>());
        }
        this.section.takenSeats.get(client).add(this);
        this.section.updateWaitlist(); // just in case
    }

    /**
     * Un-reserves a seat, making it not be taken anymore.
     * 
     * This function updates the waitlist, as it's now an available seat for
     * some other customer. It also updates the parent section's available/taken
     * seats.
     * 
     * It's only called from the `Client.returnSeat()` method.
     */
    public void returnSeat() {
        if (this.client == null) { throw new Error("Seat not taken!"); }
        this.section.takenSeats.get(this.client).remove(this);
        this.section.availableSeats.add(this);
        this.section.updateWaitlist();
        this.client = null;
    }

    /**
     * Asks the user to pick a seat.
     * This doesn't perform any reservations, only asks for a seat!
     * @param scanner The input scanner
     * @return The selected seat index
     * 
     * Apart from the basic scanner methods and print, this method only calls
     * `Seat.numberFrom(row, col)`, which converts the picked row and column
     * into a valid seat index.
     * 
     * This is called in both `SeatSection.reserveSeat()` and
     * `SeatSection.cancelReservation()`, as it's only a helper method.
     */
    public static int pick(Scanner scanner, int capacity) {
        char row = '#';
        int col = -1;

        // Input row
        System.out.print("Enter the row of your seat (A-Y), or enter 'Z' to exit: ");
        row = StadiumSeats.askForChar(scanner);
        if (row == 'z' || row == 'Z') return -1;
        while (row < 'A' || row > 'Y') {
            System.out.print("Try again, enter the row of your seat (A-Y): ");
            row = StadiumSeats.askForChar(scanner);
        }

        // Input column
        int maxColumn = capacity / 25;
        System.out.print("Enter the number of your seat (1-" + maxColumn + "): ");
        try { col = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
        while (col < 1 || col > maxColumn) {
            System.out.print("Try again, enter the number of your seat (1-" + maxColumn + "): ");
            try { col = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
        }

        // Make the seat number 
        return Seat.numberFrom(row, col);
    }

    /**
     * Converts a row and column (like "B12") into a single-integer seat index
     * @param row The given row
     * @param column The given column
     * @return The seat index, a single integer
     * 
     * This method calls no outside methods.
     * 
     * This method is only called internally, to convert row and column indices
     * into a single seat index in the constructor and seat picker.
     */
    private static int numberFrom(char row, int column) {
        return (int)(row - 'A') + (column - 1) * 25;
    }

    /**
     * Returns a seat index (single integer) as a user-friendly row and column
     * @param number The seat index
     * @return A prettified displayable seat string
     * 
     * This method calls no outside methods.
     * 
     * This method is only called in `toString()`.
     */
    private static String toStringFromNumber(int number) {
        return "" + (char)('A' + (number % 25)) + (number / 25 + 1);
    }

    /**
     * Returns the seat as a prettified string
     * @return The seat, including its section, as a prettified string.
     * 
     * This method calls `toStringFromNumber()` and the parent section's
     * `.getLevel()` to get the level as a string with no other information.
     */
    @Override
    public String toString() {
        return section.getLevel() + " " + toStringFromNumber(this.number);
    }
}
