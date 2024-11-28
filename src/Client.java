package src;

import java.util.HashSet;
import java.util.Stack;
import src.Log.TransactionType;

/**
 * The client class holds all client information: name, email, and phone number.
 * 
 * It uses a stack to hold its own history, so doing "undo" on a client only
 * affects the client the action was performed on. The stack is used because a
 * client's history only needs to be accessed in a LIFO manner. It stores a set
 * of logs, as these provide a way to print the requested undo operation and
 * store the state of purchased/cancelled. This means that you can undo a cancel
 * action!
 */
public class Client {
    String name;
    String email;
    String number;

    private HashSet<Seat> allReservedSeats = new HashSet<>();
    private Stack<Log> history = new Stack<>();

    Client(String name, String email, String number) {
        this.name = name;
        this.email = email;
        this.number = number;
    }

    /**
     * Un-does an action, whether it be a reservation or cancellation.
     * Undos themselves cannot be undone.
     * 
     * This calls either `returnSeat()` or `take()` depending on the last action
     * performed.
     * 
     * This method is only called directly by the menu in `StadiumSeats.menu()`.
     */
    public void undo() {
        if (this.history.isEmpty()) {
            System.out.println("No action to perform.");
            return;
        }

        Log action = this.history.pop();
        if (!StadiumSeats.askYesNo("Undo " + action + "?")) {
            return;
        }

        Seat seat = action.getSeat();
        if (action.getType() == TransactionType.PURCHASE) {
            this.returnSeat(seat.number);
            // Pop from history, because the above function adds a log to it
            this.history.pop();

            System.out.println("Canceled the reservation for: " + seat);
        } else {
            this.take(seat);
            // Pop from history, because the above function adds a log to it
            this.history.pop();

            System.out.println("Re-reserved the seat: " + seat);
        }
    }

    /**
     * Adds a seat to the client's history
     * @param seat The new seat
     * 
     * This method calls the given seat's `Seat.take()` method, which handles
     * all the reservations within `SeatSection`. It also adds the transaction
     * to both its personal history (for undo) and the global history (for
     * general logging).
     * 
     * This method is called by `SeatSection.reserveRandomSeat()` (which is a
     * waitlist method), `SeatSection.reserveSeat()`, and `this.undo()`.
     */
    public void take(Seat seat) {
        // Make seat "taken"
        seat.take(this);

        // Add to reservations
        this.allReservedSeats.add(seat);

        // Add to log
        Log log = new Log(this, seat, TransactionType.PURCHASE);
        this.history.push(log);
        StadiumSeats.history.add(log);
    }

    /**
     * Un-reserves a reserved seat, canceling its reservation.
     * @param seatIndex The index of the seat to be returned
     * @return The seat that was un-reserved
     * 
     * This method calls `Seat.returnSeat()` which itself handles the movement
     * within `SeatSection`. It also adds the transaction to both its personal
     * history (for undo) and the global history (for general logging).
     * 
     * This is called from `SeatSection.cancelReservation()` and
     * `this.undo()`.
     */
    public Seat returnSeat(int seatIndex) {
        // Find and remove seat from reserved set (fast)
        Seat seat = null;
        for (Seat s : this.allReservedSeats) {
            if (s.number == seatIndex) {
                seat = s;
                break;
            }
        }
        if (seat == null) return null;
        this.allReservedSeats.remove(seat);

        // Make seat available
        seat.returnSeat();

        // Add to log
        Log log = new Log(seat.client, seat, TransactionType.CANCEL);
        this.history.add(log);
        StadiumSeats.history.add(log);

        return seat;
    }

    /**
     * Gets the amount of seats this client has taken.
     * @return The amount of reserved seats
     * Called by `SeatSection.cancelReservation()` to ensure the client has
     * seats to cancel before prompting the user.
     */
    public int getTakenSeats() {
        return this.allReservedSeats.size();
    }

    /** Displays the client as a prettified string. */
    @Override
    public String toString() {
        return this.name + " (" + this.email + ")";
    }
}
