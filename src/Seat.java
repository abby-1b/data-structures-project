package src;

public class Seat {
    SeatSection section;
    int number;

    public Seat(SeatSection section, int number) {
        this.section = section;
        this.number = number;
    }

    public Seat() {}

    private static String displayIdFromNumber(int number) {
        return "" + (char)('A' + (number % 25)) + (number / 25 + 1);
    }

    @Override
    public String toString() {
        return "[" + section + "] Number: " + number + " (" + displayIdFromNumber(this.number) + ")";
    }

    public static String toStringFromNumber(int number) {
        return displayIdFromNumber(number);
    }
}
