package src;

public class Seat {
    SeatSection section;
    int number;

    public Seat(SeatSection section, int number) {
        this.section = section;
        this.number = number;
    }
    public Seat(SeatSection section, char row, int column) {
        this.section = section;
        this.number = Seat.numberFrom(row, column);
    }

    public static int numberFrom(char row, int column) {
        return (int)(row - 'A') + (column - 1) * 25;
    }
    private static String displayIdFromNumber(int number) {
        return "" + (char)('A' + (number % 25)) + (number / 25 + 1);
    }

    @Override
    public String toString() {
        return section.getLevel() + " " + displayIdFromNumber(this.number) + "(" + this.number + ")";
    }

    public static String toStringFromNumber(int number) {
        return displayIdFromNumber(number);
    }
}
