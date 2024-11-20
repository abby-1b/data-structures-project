// package src.main;
/* 
TODO: Apparently the mf wants individual seats with sections and seat numbers, when the instructions say 2000+ seats
      I don't know how they expect to add 2000 seats one by one in a set
 */ 

public class Seats {
    String level;
    double cost;
    int capacity;

    Seats(String level, double cost, int capacity) {
        this.level = level;
        this.cost = cost;
        this.capacity = capacity;
    }

    void setCapacity(int capacity) { this.capacity = capacity; }

    void printData() {
        System.out.println("Level: " + level + " Seat Cost: $" + cost + " Available: " + capacity);
    }
}
