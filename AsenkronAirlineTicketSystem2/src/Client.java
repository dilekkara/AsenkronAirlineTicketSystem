import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    Socket clientSocket = null;
    int clientID; // İstemcinin kimlik numarası
    int portNumber; // İstemcinin port numarası
    int serverPortNumber; // Sunucunun port numarası
    int processOrder; // İşlem sırası (0: Okuma, 1: Yazma, 2: Yazma-İptal-Okuma)
    int requestedTicket; // Talep edilen bilet numarası
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Message receivedMsg = null;

    // Client constructor
    public Client(int clientID, int portNumber, int serverPortNumber, int requestedTicket, int processOrder) {
        this.clientID = clientID;
        this.portNumber = portNumber;
        this.serverPortNumber = serverPortNumber;
        this.processOrder = processOrder;
        this.requestedTicket = requestedTicket;
    }

    // Bilet rezervasyonu yapmak için kullanılan fonksiyon
    private void WriterThread() {
        try {
            System.out.println("Time: " + java.time.LocalTime.now());
            System.out.println("Writer" + clientID + "   " + (requestedTicket + 1) + " numaralı bileti alabilmek için istekte bulunuyor.....");
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setContent(requestedTicket);
            requestedMsg.setType(Message.Type.SERVICE_MAKE_REZERVATION);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("Writer" + clientID + " Sunucudan " + receivedMsg.getType() + " tipinde bir mesaj içeriği aldı : mesaj --> " + (String) receivedMsg.getContent());
            System.out.println("--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bilet iptal etmek için kullanılan fonksiyon
    private void CancelThread() {
        try {
            System.out.println("Time: " + java.time.LocalTime.now());
            System.out.println("CancelWriter" + clientID + "   " + (requestedTicket + 1) + " numaralı biletini iptal etmek için istekte bulunuyor...");
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setContent(requestedTicket);
            requestedMsg.setType(Message.Type.SERVICE_CANCEL_REZERVATION);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);

            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("CancelWriter" + clientID + " Sunucudan " + receivedMsg.getType() + " tipinde bir mesaj içeriği aldı : mesaj --> " + (String) receivedMsg.getContent());
            System.out.println("--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bilet listesini okumak için kullanılan fonksiyon
    private void ReaderThread() {
        try {
            System.out.println("Time: " + java.time.LocalTime.now());
            System.out.println("Reader" + clientID + " bilet alabilmek için bilet listesini okuma isteğinde bulunuyor...");
            clientSocket = new Socket("localhost", serverPortNumber);
            Message requestedMsg = new Message();
            requestedMsg.setSenderID(clientID);
            requestedMsg.setSenderPortNumber(portNumber);
            requestedMsg.setType(Message.Type.SERVICE_READ_REZERVATION_LIST);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(requestedMsg);
            ois = new ObjectInputStream(clientSocket.getInputStream());
            receivedMsg = (Message) ois.readObject();
            System.out.println("Reader" + clientID + " Sunucudan " + receivedMsg.getType() + " tipinde bir mesaj içeriği aldı.");
            Flight flight = (Flight) receivedMsg.getContent();
            System.out.println("*****Bilet Numaraları*****");
            for (int j = 0; j < flight.getTicketList().length; j++) {
                String ticketNumber = flight.getTicketList()[j].getTicketNumber();
                Boolean ticketState = flight.getTicketList()[j].isTicketState();

                if (ticketState == false) {
                    System.out.print(ticketNumber + " : Boş *--* ");
                } else {
                    System.out.print(ticketNumber + " : Dolu *--* ");
                }
            }
            System.out.println("\n--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // İstemcinin çalıştırılmasını sağlayan run metodu
    @Override
    public void run() {
        try {
            if (processOrder == 0) {
                ReaderThread(); // İlk önce okuma işlemi
                Thread.sleep(50); // Bekleme süresi
                WriterThread(); // Sonra yazma işlemi
            } else if (processOrder == 1) {
                WriterThread(); // İlk önce yazma işlemi
                Thread.sleep(100); // Bekleme süresi
                ReaderThread(); // Sonra okuma işlemi
            } else if (processOrder == 2) {
                WriterThread(); // Yazma işlemi
                Thread.sleep(50); // Bekleme süresi
                CancelThread(); // İptal işlemi
                Thread.sleep(50); // Bekleme süresi
                ReaderThread(); // Okuma işlemi
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
