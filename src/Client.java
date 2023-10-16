import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {

    Socket client;
    Thread listenerThread;
    String username = "Anonymous";

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
                    if (str.startsWith("/")){
                        String[] input = str.split(" ");
                        if (input[0].equals("/username")) {
                            if (input.length == 1)
                                System.err.println("Username not provided!");
                            else {
                                if (input[1].length() <= 2)
                                    System.err.println("Username must be at least 2 characters!");
                                else
                                    username = input[1];
                            }
                        }
                        if (input[0].equals("/me")){
                            if (input.length == 1)
                                System.err.println("Please provide text you wish to /me!");
                            else
                                out.println("*" + username + str.substring(3) + "*");
                        }
                    } else {
                        out.println("<" + username + "> " + str);
                    }
                }
            }

            client.close();

        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
