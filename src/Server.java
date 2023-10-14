import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server { /* starting with the connection
    skeleton class of the server */

    ServerSocket server; // designated chatroom server
    ArrayList<RoomConnection> connections; // list of connections

    public Server () {
        try {
            System.out.println("Waiting for clients");
            connections = new ArrayList<>();
            server = new ServerSocket(8080);
            Socket client = server.accept();
            System.out.println("connection established");
            RoomConnection incomingConnection = new RoomConnection(client);
            connections.add(incomingConnection);
            new Thread(incomingConnection).start();
        } catch (Exception totalFailure) {
            closeServer();
        }
    }

    public void closeServer () {
        for (RoomConnection c : connections) {
            if (c != null) {
                c.disconnect();
            }
        }
        try { server.close(); }
        catch (Exception ignored) {}
    }

    public void broadcast (String msg) {
        for (RoomConnection c : connections) {
            if (c != null) {
                c.sendMessageTo(msg);
            }
        }
    }

    class RoomConnection implements Runnable {

        Socket client; // assigned socket of connection
        Scanner in; // client input
        PrintWriter out; // client output


        public RoomConnection (Socket clientSocket) {
            this.client = clientSocket;
        }

        public void sendMessageTo (String msg) {
            out.println(msg);
        }

        public void processMessages() {
            try {
                in = new Scanner(client.getInputStream());
                out = new PrintWriter(client.getOutputStream(),true);
                System.out.println("Client connected");
                String toSend;
                while (in.hasNextLine()) {
                    toSend = in.nextLine();
                    if (toSend.equals("\\quit")) {
                        disconnect();
                    } else broadcast(toSend);
                }
            } catch (Exception ignored) {}
        }

        public void disconnect () {
            try {
                sendMessageTo("You've been disconnected");
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
                Thread.currentThread().join();
            } catch (Exception ignored) {}
        }

        @Override
        public void run() {
            processMessages();
        }
    }

    public static void main(String[] args) {
        Server mainServer = new Server();
    }
}
