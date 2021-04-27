import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Server for the family map. Connects to port 8080
 */
public class Server {

    /**
     * Number of connections which can be queued
     */
    private static final int MAX_WAITING_CONNECTIONS = 12;

    /**
     * HTTP server which can receive incoming and outgoing HTTP requests
     * and responses.  Implements HTTP network protocol.
     */
    public HttpServer server;

    /**
     * Initializes and runs HTTP server
     *
     * @param portNumber port number to accept client connections
     */
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating Contexts");
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/", new FileHandler());

        System.out.println("Starting Server");
        server.start();
        System.out.println("Server Started");
    }

    /**
     * Main function of the server which has one command line argument
     *
     * @param args arguments which contains the port number to accept client connections
     */
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
