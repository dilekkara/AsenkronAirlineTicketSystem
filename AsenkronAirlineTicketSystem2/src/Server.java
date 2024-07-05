import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Flight flight = null;
    public static Ticket ticket = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        fillMyInfo(); // Uçuş bilgilerini doldur
        int serverPortNumber = 8082; // Sunucunun port numarası
        ServerSocket serverSocket = new ServerSocket(serverPortNumber);
        Message receivedMsg = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        System.out.println(serverSocket.getLocalPort() + " numaralı porttan gelen istekleri bekliyorum...");
        boolean listening = true; // Sunucunun dinleme durumunu belirler
        while (listening) {
            Socket connectionSocket = serverSocket.accept(); // Gelen bağlantı isteklerini kabul et
            ois = new ObjectInputStream(connectionSocket.getInputStream());
            receivedMsg = (Message) ois.readObject(); // İstemciden gelen mesajı oku
            ClientHandler thread = new ClientHandler(connectionSocket, receivedMsg, flight); // Yeni bir ClientHandler thread oluştur
            thread.start(); // Thread'i başlat
        }
        serverSocket.close(); // Sunucu soketini kapat
    }

    // Uçuş bilgilerini doldurmak için kullanılan fonksiyon
    public static void fillMyInfo() {
        flight = new Flight();
        flight.setFlightID(1);
        flight.setFlightDate("10.07.2024");
        flight.setRoute("İstanbul-Samsun");
        Ticket[] ticketList = new Ticket[5];
        for (int i = 1; i < 6; i++) {
            ticket = new Ticket();
            ticket.setTicketID(i);
            if (i % 2 == 0) {
                ticket.setTicketNumber(i + ".numara-Cam Kenarı");
            } else {
                ticket.setTicketNumber(i + ".numara-Koridor");
            }
            ticket.setTicketState(false); // Biletin durumu (false: boş, true: dolu)
            ticket.setTicketHolder(0); // Biletin sahibi (0: boş)
            ticketList[i - 1] = ticket;
        }
        flight.setTicketList(ticketList); // Uçuşa bilet listesini ata
    }
}
