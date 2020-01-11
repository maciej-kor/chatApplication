package server;


public class ServerMain {

    private static final int PORT = 9999;

    public static void main(String[] args) {

        Server server = new Server(PORT);
        server.start();

    }

}
