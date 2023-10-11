import java.net.*;

public class Client {

    Socket client;

    public Client() {
        try {
            client = new Socket("localHost", 8080);
            client.close();
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
