import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {

    Socket client;
    Thread listenerThread;

    public Client() {
        try {
            System.out.println("client started");
            client = new Socket("localHost", 8080);
            BufferedReader userInput, in;
            userInput = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            listenerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            if (!client.isConnected()) {
                                in.close();
                                break;
                            }else if (in.ready()) {
                                System.out.println(in.readLine());
                            }
                        } catch (Exception failuretoRead) {}
                    }
                }
            });

            listenerThread.start();
            System.out.println("Enter a string to send a message");

            while (true) {
                if (!client.isConnected()) {
                    userInput.close();
                    out.close();
                    break;
                }
                if (userInput.ready()) {
                    String str = userInput.readLine();
                    out.println(str);
                }
            }

            client.close();

        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
