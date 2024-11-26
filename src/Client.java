package src;

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

    Client() {
        this.name = "name";
        this.email = "email@mail.com";
        this.number = "000-000-0001";
    }

    void printData() {
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + number);
    }
}
