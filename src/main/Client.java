// package src.main;
public class Client {
    String name;
    String email;
    String number;

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
}
