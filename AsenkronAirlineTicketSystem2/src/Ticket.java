import java.io.Serializable;

public class Ticket implements Serializable {
    private int ticketID;
    private String ticketNumber;
    private boolean ticketState;


    private int ticketHolder;

    public Ticket() {
    }

    public Ticket(int ticketID, String ticketNumber, boolean ticketState, int ticketHolder) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.ticketState = ticketState;
        this.ticketHolder = ticketHolder;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public boolean isTicketState() {
        return ticketState;
    }

    public void setTicketState(boolean ticketState) {
        this.ticketState = ticketState;
    }

    public int getTicketHolder() {
        return ticketHolder;
    }

    public void setTicketHolder(int ticketHolder) {
        this.ticketHolder = ticketHolder;
    }


}