package src;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import src.Log.TransactionType;

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

    public void cancelReservation(Scanner scanner, Client client) {
        if (this.getTaken() == 0) {
            System.out.println("Sorry, there are no seats to cancel. All the seats are empty!");
            askForWaitlist(scanner, client);
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

    public void reserveSeat(Scanner scanner, Client client) {
        if (this.getRemaining() == 0) {
            System.out.println("Sorry, there is no more space in the requested section.");
            askForWaitlist(scanner, client);
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

    public void askForWaitlist(Scanner scanner, Client client) {
        if (StadiumSeats.askYesNo("Would you like to enter the waitlist?")) {
            addToWaitlist(client);
        }
    }

    
    public void addToWaitlist(Client client) {
        this.waitList.add(client);
        updateWaitlist();
    }
    
    public Seat reserveRandomSeat(Client client) {
        if (this.availableSeats.size() == 0) return null;
        Seat foundSeat = this.availableSeats.iterator().next();
        this.availableSeats.remove(foundSeat);
        foundSeat.take(client);
        return foundSeat;
    }

    /** Updates the waitlist, granting random seats to waitlisted clients. */
    public void updateWaitlist() {
        while (this.getRemaining() > 0 && this.waitList.size() > 0) {
            this.reserveRandomSeat(this.waitList.remove());
        }
    }

    public int getCapacity() {
        return capacity;
    }
    public int getRemaining() {
        return this.availableSeats.size();
    }
    public int getTaken() {
        return this.takenSeats.size();
    }

    public String getDisplayID() {
        return this.level;
    }

    public void displayAvailability() {
        System.out.println("  " + this);
    }

    @Override
    public String toString() {
        return "[" + level + "] " + cost + " (" + getRemaining() + " available)";
    }

    public String getLevel() {
        return this.level;
    }

    public Seat containsSeat(Client client, int seatNum) {
        if (this.takenSeats.containsKey(client)) {
            for (Seat s : this.takenSeats.get(client)) {
                if (s.number == seatNum) {
                    return s;
                }
            }
        }

        return null;
    }

}
