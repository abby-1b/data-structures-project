package src;

import java.util.Stack;
// package src.main;
public class Client {
    String name;
    String email;
    String number;
    Stack<Seat> reservedSeats = new Stack<>();

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

    @Override
    public String toString() {
        return this.name;
    }
}
