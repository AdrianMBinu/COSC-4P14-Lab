import java.net.*;

public class Client {

    Socket client;

    public Client() {
        try {
            System.out.println("client started");
            client = new Socket("localHost", 8080);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("enter a string");
            String str = userInput.readLine();
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);
            out.println(str);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println(in.readLine());
            
            client.close();
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        Client c = new Client();
    }
}
