package src;

import java.util.HashSet;
import java.util.Scanner;

public class Seat {
    SeatSection section;
    int number;
    Client client; // The client this seat belongs to

    public Seat(SeatSection section, int number) {
        this.section = section;
        this.number = number;
    }
    public Seat(SeatSection section, char row, int column) {
        this.section = section;
        this.number = Seat.numberFrom(row, column);
    }

    /**
     * Takes a seat, making it unavailable
     * @param client
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
     * Returns a seat, making it not be "taken" anymore
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
     * This doesn't perform any reservations, only asks for a seat.
     * @param scanner The input scanner
     * @return The selected seat index
     */
    public static int pick(Scanner scanner, int capacity) {
        char row = '#';
        int col = -1;

        // Input row
        System.out.print("Enter the row of your seat (A-Y), or enter 'Z' to exit: ");
        row = scanner.nextLine().charAt(0);
        if (row == 'z' || row == 'Z') return -1;
        while (row < 'A' || row > 'Y') {
            System.out.print("Try again, enter the row of your seat (A-Y): ");
            row = scanner.nextLine().charAt(0);
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
     */
    public static int numberFrom(char row, int column) {
        return (int)(row - 'A') + (column - 1) * 25;
    }

    /**
     * Displays a seat index (single integer) as a user-friendly row and column
     * @param number The seat index
     * @return 
     */
    private static String toStringFromNumber(int number) {
        return "" + (char)('A' + (number % 25)) + (number / 25 + 1);
    }

    /** Returns the seat as a prettified string */
    @Override
    public String toString() {
        return section.getLevel() + " " + toStringFromNumber(this.number);
    }
}
