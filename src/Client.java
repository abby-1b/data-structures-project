package src;

import java.util.HashSet;
import java.util.Stack;
import src.Log.TransactionType;

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

    void printData() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + number);
    }

    public void undo() {
        if (this.history.isEmpty()) {
            System.out.println("No action to perform.");
            return;
        }

        Log action = this.history.pop();
        if (!StadiumSeats.askYesNo("Would you like to undo " + action + "?")) {
            return;
        }

        Seat seat = action.getSeat();
        if (action.getType() == TransactionType.PURCHASE) {
            this.returnSeat(seat.number);
            System.out.println("You have canceled the reservation for: " + seat);
            this.history.pop();
        } else {
            this.take(seat);
            System.out.println("You have re-taken the seat: " + seat);
            this.history.pop();
        }
    }

    /**
     * Adds a seat to the client's history
     * @param seat The new seat
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
     * Remoun-reserves a seat
     * @param seatIndex
     * @return
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

    @Override
    public String toString() {
        return this.name;
    }
}
