package src;

/**
 * Records transactions made by clients. 
 * 
 * Each log entry contains details about the client, the seat they interacted
 * with, and the type of transaction (purchase or cancellation).
 */
public class Log {

    /**
     * Represents the type of a transaction
     *  - PURCHASE: Indicates that the client purchased a seat
     *  - CANCEL: Indicates that the client canceled a reservation
     */
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

    /**
     * Gets the seat involved in this log entry.
     * @return The seat that was purchased or canceled.
     * 
     * Only called by `Client.undo()` to perform actions on a log (history).
     */
    public Seat getSeat() { return seat; }

    /**
     * Gets the type of transaction for this log entry.
     * @return The type of transaction (PURCHASE or CANCEL).
     * 
     * Only called by `Client.undo()` to perform actions on a log (history).
     */
    public TransactionType getType() { return this.type; }

    @Override
    public String toString() {
        return "[" + this.client + "] " +
            (this.type == TransactionType.PURCHASE ? "purchased" : "canceled") +
            this.seat;
    }
}
