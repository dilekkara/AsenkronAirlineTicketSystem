import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket socket; // İstemciyle olan bağlantı
    Message receivedMsg; // İstemciden alınan mesaj
    Flight flight; // Uçuş bilgileri

    // ClientHandler constructor
    public ClientHandler(Socket socket, Message receivedMsg, Flight flight) {
        this.socket = socket;
        this.receivedMsg = receivedMsg;
        this.flight = flight;
    }

    // Thread çalıştırıldığında yapılan işlemler
    public void run() {
        try {
            Message sendMsg = null;
            ObjectOutputStream oos = null;
            System.out.println("\n " + receivedMsg.getSenderID() + " numaralı Client'dan "
                    + receivedMsg.getType() +
                    " tipinde bir mesaj aldım. ");
            // Bilet rezervasyon isteği
            if (receivedMsg.getType() == Message.Type.SERVICE_MAKE_REZERVATION) {
                int rcvticketID = (int) receivedMsg.getContent();
                Ticket ticket = flight.getTicketList()[rcvticketID];
                if (ticket.isTicketState()) { // Bilet zaten rezerve edilmişse
                    sendMsg = new Message();
                    sendMsg.setSenderPortNumber(socket.getPort());
                    String response = "Almaya Çalıştığınız " + ticket.getTicketNumber() + " numaralı bilet başka birisi tarafından alındı";
                    sendMsg.setContent(response);
                    sendMsg.setType(Message.Type.SERVICE_RESPONSE_UNSUCCESSFUL);
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(sendMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Bilet rezerve edilmemişse
                    ticket.setTicketState(true);
                    ticket.setTicketHolder(receivedMsg.getSenderID());
                    sendMsg = new Message();
                    sendMsg.setSenderPortNumber(socket.getPort());
                    String response = ticket.getTicketNumber() + " numaralı bileti başarıyla aldınız";
                    sendMsg.setContent(response);
                    sendMsg.setType(Message.Type.SERVICE_RESPONSE_SUCCESSFUL);
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(sendMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            // Bilet iptal isteği
            } else if (receivedMsg.getType() == Message.Type.SERVICE_CANCEL_REZERVATION) {
                int rcvticketID = (int) receivedMsg.getContent();
                Ticket ticket = flight.getTicketList()[rcvticketID];
                if (ticket.isTicketState() && ticket.getTicketHolder() == receivedMsg.getSenderID()) {
                    ticket.setTicketState(false);
                    ticket.setTicketHolder(0);
                    sendMsg = new Message();
                    sendMsg.setSenderPortNumber(socket.getPort());
                    String response = ticket.getTicketNumber() + " numaralı biletiniz başarıyla iptal edildi";
                    sendMsg.setContent(response);
                    sendMsg.setType(Message.Type.SERVICE_RESPONSE_SUCCESSFUL);
                    try {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(sendMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            // Bilet listesini okuma isteği
            } else if (receivedMsg.getType() == Message.Type.SERVICE_READ_REZERVATION_LIST) {
                sendMsg = new Message();
                sendMsg.setSenderPortNumber(socket.getPort());
                sendMsg.setContent(flight);
                sendMsg.setType(Message.Type.SERVICE_RESPONSE_SUCCESSFUL);
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(sendMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
