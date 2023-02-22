package server;

import java.io.IOException;

public class Main {

    public static Server server;

    public static void main(String[] args) throws IOException {

        // Start a new server
        server = new Server();
        server.start();
    }

}
