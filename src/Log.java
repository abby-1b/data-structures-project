package src;

public class Log {
    private Client client;
    private Seat seat;

    public Log(Client client, Seat seat) {
        this.client = client;
        this.seat = seat;
    }

    public Client getClient() { return client; }
    public Seat getSeat() { return seat; }
    public int getSeatNum() { return seat.number; }
    public double getSeatCost() { return seat.section.getCost(); }

}
