package science.yurimdias.socket;

public class Entrypoint {

    public static void main(String[] args) {
        // Receive as arguments?
        final String socket_address = "localhost";
        final int socket_port = 9999;
        SocketServer server = new SocketServer();
        System.out.println("Starting server...");
        server.start(socket_address, socket_port);
        // Shouldn't be necessary due to OS, actually
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdown()));
    }

}
