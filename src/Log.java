package src;

public class Log {
    public enum TransactionType {
        PURCHASE,
        CANCEL,
    }

    private Client client;
    private Seat seat;
    private TransactionType type;

    public Log(Client client, Seat seat, TransactionType type) {
        this.client = client;
        this.seat = seat;
        this.type = type;
    }

    public Client getClient() { return client; }
    public Seat getSeat() { return seat; }
    public int getSeatNum() { return seat.number; }
    public TransactionType getType() { return this.type; }

    @Override
    public String toString() {
        String out = "[" + this.client + "] ";
        if (this.type == TransactionType.PURCHASE) {
            out += "purchased";
        } else {
            out += "canceled";
        }
        out += this.seat;
        return out;
    }
}
