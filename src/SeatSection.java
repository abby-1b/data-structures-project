package src;
import java.util.HashSet;

public class SeatSection {
    private String level;
    private double cost;
    private int remainingSeats;
    private HashSet<Seat> seats = new HashSet<>();

    public SeatSection(String level, double cost, int capacity) {
        this.level = level;
        this.cost = cost;
        this.remainingSeats = capacity;
    }

    public Seat reserveRandomSeat() {
        int number = (int)(Math.random() * this.getCapacity());
        return this.reserveSeat(number);
    }

    public Seat reserveSeat(int number) {
        // Java can use this as an object?! 
        Seat seat = new Seat(this, number);
        seats.add(seat);
        remainingSeats--;
        return seat;
    }

    public void remove(Seat s) { 
        seats.remove(s); 
        remainingSeats++;
    }

    public int getCapacity() {
        return this.remainingSeats + this.seats.size();
    }

    public boolean contains(Seat s) { return seats.contains(s); }

    public String getDisplayID() {
        return this.level;
    }

    @Override
    public String toString() {
        return "[" + level + "] Seat Cost: $" + cost + " Available: " + remainingSeats;
    }
}
