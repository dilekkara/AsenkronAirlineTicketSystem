public class ClientMain {

    public static void main(String[] args) throws InterruptedException {
        int serverPortNumber = 8082; // Sunucunun port numarası
        int requestedTicket = 0; // Talep edilen bilet numarası

        // Üç farklı istemci oluşturuluyor
        Client client1 = new Client(1, 7500, serverPortNumber, requestedTicket, 1);
        Client client2 = new Client(2, 7600, serverPortNumber, requestedTicket, 0);
        Client client3 = new Client(3, 7700, serverPortNumber, requestedTicket, 0);

        // İstemci thread'leri oluşturuluyor
        Thread thread1 = new Thread(client1);
        Thread thread2 = new Thread(client2);
        Thread thread3 = new Thread(client3);

        // Thread'ler başlatılıyor
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
