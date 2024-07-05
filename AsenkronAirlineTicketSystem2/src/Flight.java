import java.io.Serializable;

public class Flight implements Serializable {
    private int flightID;
    private String flightDate;
    private String route;
    private Ticket[] ticketList;
    public Flight(){

    }
    public Flight ( int flightID, String flightDate, String route, Ticket[] ticketList) {
        this.flightID = flightID;
        this.flightDate = flightDate;
        this.route = route;
        this.ticketList = ticketList;
    }
    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Ticket[] getTicketList() {
        return ticketList;
    }

    public void setTicketList(Ticket[] ticketList) {
        this.ticketList = ticketList;
    }


}