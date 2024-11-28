package src;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * The specific section of seating in the stadium. Each section has a level,
 * cost, and capacity, and manages its own seats, reservations, and a waitlist
 * for clients.
 * 
 * A HashSet was used to stores all available seats. This is a bit slow, as the
 * hashing and equality functions aren't implemented for the Seat object and
 * therefore the set has to be searched through manually.
 * 
 * A HashMap was used to map Clients to a collection of their reserved seats.
 * 
 * A Queue of clients was used to hold the waitlist for clients wanting a seat
 * when the section is full. This doesn't let clients reserve a specific seat,
 * only a random seat within the stadium.
 */
public class SeatSection {
    private String level;
    private String cost;
    private int capacity;

    public HashSet<Seat> availableSeats = new HashSet<>();
    public HashMap<Client, HashSet<Seat>> takenSeats = new HashMap<>();

    public Queue<Client> waitList = new LinkedList<>();
    
    public SeatSection(String level, String cost, int capacity) {
        this.level = level;
        this.cost = cost;
        this.capacity = capacity;

        // Setup the seats
        for (int i = 0; i < capacity; i++) {
            availableSeats.add(new Seat(this, i));
        }
    }

    /**
     * Cancels a seat reservation for a client
     * @param scanner A scanner object used for getting user input
     * @param client The client we're canceling a reservation for
     * 
     * It uses `Seat.pick()` to select a seat (within this section's range), and
     * then calls `Client.returnSeat()` to cancel the reservation.
     * 
     * Only called by `StadiumSeats.cancelReservation()`.
     */
    public void cancelReservation(Scanner scanner, Client client) {
        if (this.getTaken() == 0) {
            System.out.println("Sorry, there are no seats to cancel. All the seats are empty!");
            return;
        } else if (client.getTakenSeats() == 0) {
            System.out.println("Sorry, this client doesn't have seats in this section.");
            return;
        }

        while (true) {
            // Pick a seat
            int seatIndex = Seat.pick(scanner, this.getCapacity());
            if (seatIndex == -1) return;

            // Ensure the seat is available
            Seat seatToRemove = client.returnSeat(seatIndex);
            if (seatToRemove == null) {
                System.out.println("This seat has not been reserved.");
            } else {
                System.out.println("Canceled the reservation for: " + seatToRemove);
                break;
            }
        }
    }

    /**
     * Reserves a seat for a client
     * @param scanner A scanner object used for getting user input
     * @param client The client we're making a reservation for
     * 
     * If no seats are available, this calls `askForWaitlist()` to prompt for
     * joining this section's waitlist.
     * 
     * Only called from `StadiumSeats.purchaseSeat()`.
     */
    public void reserveSeat(Scanner scanner, Client client) {
        if (this.getRemaining() == 0) {
            System.out.println("Sorry, there is no more space in the requested section.");
            askForWaitlist(scanner, client);
            return;
        }

        while (true) {
            // Pick a seat
            int seatIndex = Seat.pick(scanner, this.getCapacity());
            if (seatIndex == -1) return;

            // Ensure the seat is available
            Seat reservedSeat = this.tryGetAvailableSeat(seatIndex);
            if (reservedSeat == null) {
                System.out.println("This seat is not available.");
                continue;
            } else {
                client.take(reservedSeat);
                System.out.println("Successfully purchased " + reservedSeat);
                System.out.println("This cost " + this.cost);
                break;
            }

        }
    }

    /**
     * Attempts to find an available seat
     * @param number The number of the seat to check
     * @return The seat (if available), null if the seat is already reserved
     * 
     * This is only called from `reserveSeat()`.
     */
    public Seat tryGetAvailableSeat(int number) {
        // Find the seat (if any)
        Seat foundSeat = null;
        for (Seat s : this.availableSeats) {
            if (s.number == number) {
                foundSeat = s;
                break;
            }
        }
        return foundSeat;
    }

    /**
     * Asks the client if they want to join the waitlist
     * @param scanner A scanner object used for getting user input
     * @param client The client that might join the waitlist
     * 
     * Only caled by `reserveSeat()`.
     */
    public void askForWaitlist(Scanner scanner, Client client) {
        if (StadiumSeats.askYesNo("Would you like to enter the waitlist?")) {
            this.waitList.add(client);
            updateWaitlist(); // just in case
        }
    }

    /**
     * Updates the waitlist, granting random seats to waitlisted clients when
     * they're available.
     * 
     * Called when a seat becomes available, from `Seat.returnSeat()`.
     */
    public void updateWaitlist() {
        Iterator<Seat> seatIterator = this.availableSeats.iterator();
        while (this.getRemaining() > 0 && this.waitList.size() > 0) {
            Seat seat = seatIterator.next();
            this.waitList.remove().take(seat);
        }
    }

    /**
     * Gets the total capcity in this section.
     * @return The capacity
     * Called by `reserveSeat()` and `cancelReservation()` to get the range of
     * seats the user can input.
     */
    public int getCapacity() { return capacity; }
    /**
     * Gets the remaining amount of seats.
     * @return The amount of available seats
     * Called by `reserveSeat()` and `updateWaitlist()`.
     */
    public int getRemaining() {
        return this.availableSeats.size();
    }
    /**
     * Gets the amount of taken seats.
     * @return The amount of reserved seats
     * Called by `cancelReservation()` to ensure there are seats to cancel.
     */
    public int getTaken() {
        return this.takenSeats.size();
    }

    /**
     * Displays this section's availability.
     * 
     * Called by `StadiumSeats.displayAvailability()` to show the availability
     * of all three sections.
     */
    public void displayAvailability() {
        System.out.println("  " + this);
    }

    /**
     * Gets this section's level name. Used to show less information than the
     * already-existing `toString()` method.
     * @return The level name with no other formatting
     */
    public String getLevel() {
        return this.level;
    }

    @Override
    public String toString() {
        return "[" + level + "] " + cost + " (" + getRemaining() + " available)";
    }

}
