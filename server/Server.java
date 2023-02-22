package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static final String ADDRESS = "127.0.0.1";
    static final int PORT = 23456;
    private final ServerSocket server;
    private final ExecutorService executor;

    public Server() throws IOException {
        this.server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() {

        System.out.println("Server started!");

        try {
            while (!executor.isShutdown()) {
                executor.submit(new RequestHandler(server.accept()));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void stop() {
        try {
            executor.shutdown();
            server.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
