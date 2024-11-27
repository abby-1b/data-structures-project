package src;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SeatSection {
    private String level;
    private String cost;
    private int capacity;

    public HashSet<Seat> availableSeats = new HashSet<>();
    public HashMap<Client, ArrayList<Seat>> takenSeats = new HashMap<>();

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

    public void pickSeat(Scanner scanner, Client client) {
        if (this.getRemaining() < 0) {
            System.out.println("Sorry, there is no more space in the requested section.");
            askForWaitlist(scanner, client);
        }

        while (true) {
            char row = '#';
            int col = -1;
    
            // Input row
            System.out.print("Enter the row of your seat (A-Y); or enter Z to exit: ");
            row = scanner.nextLine().charAt(0);
            if (row == 'z' || row == 'Z') return;
            while (row < 'A' || row > 'Y') {
                System.out.print("Try again, enter the row of your seat (A-Y): ");
                row = scanner.nextLine().charAt(0);
            }
    
            // Input column
            int maxColumn = this.getCapacity() / 25;
            System.out.print("Enter the number of your seat (1-" + maxColumn + "): ");
            try { col = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            while (col < 1 || col > maxColumn) {
                System.out.print("Try again, enter the number of your seat (1-" + maxColumn + "): ");
                try { col = Integer.parseInt(scanner.nextLine()); } catch (Exception e) {}
            }

            // Ensure the seat is available
            int number = Seat.numberFrom(row, col);
            Seat reservedSeat = this.reserveSeat(client, number);
            if (reservedSeat == null) {
                System.out.println("This seat is not available.");
                continue;
            } else {
                System.out.println("Successfully purchased " + reservedSeat);
                break;
            }

        }
    }
    public void askForWaitlist(Scanner scanner, Client client) {
        while (true) {
            System.out.println("Would you like to enter the waitlist?");
            switch (scanner.nextLine().toLowerCase()) {
                case "yes", "y" -> {
                    addToWaitlist(client);
                    return;
                }
                case "no", "n" -> { return; }
                default -> System.out.println("Please enter a valid option. (Y/N)");
            }
        }
    }

    public Seat reserveRandomSeat(Client client) {
        if (this.availableSeats.size() == 0) return null;
        Seat foundSeat = this.availableSeats.iterator().next();
        this.availableSeats.remove(foundSeat);
        this.takeSeat(client, foundSeat);
        return foundSeat;
    }

    public Seat reserveSeat(Client client, int number) {
        Seat foundSeat = null;
        for (Seat s : this.availableSeats) {
            if (s.number == number) {
                foundSeat = s;
                break;
            }
        }

        if (foundSeat == null) return null;

        // Make seat "taken"
        this.availableSeats.remove(foundSeat);
        this.takeSeat(client, foundSeat);

        return foundSeat;
    }

    public void addToWaitlist(Client client) {
        this.waitList.add(client);
        updateWaitlist();
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

    public void takeSeat(Client client, Seat seat) {
        if (this.takenSeats.containsKey(client)) {
            this.takenSeats.get(client).add(seat);
        }
        else {
            this.takenSeats.put(client, new ArrayList<>());
            this.takenSeats.get(client).add(seat);
        }
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
