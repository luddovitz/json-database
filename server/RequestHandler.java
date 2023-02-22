package server;

import client.RequestObject;
import com.google.gson.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class RequestHandler implements Runnable {

    private final Database DATABASE = new Database();
    private final Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
        try {
            DATABASE.init();
        } catch (IOException e) {
            throw new RuntimeException("Database failed to start");
        }
    }

    @Override
    public void run() {

        try {

            // Open IO Streams
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Get request
            RequestObject request = new Gson().fromJson(input.readUTF(), RequestObject.class);

            // Print received
            System.out.println("Received: " + new Gson().toJson(request));

            // Init response
            String response = "";

            switch (request.getType().toUpperCase()) {
                case "GET" -> response = DATABASE.get(request.getKey());
                case "SET" -> response = DATABASE.set(request.getKey(), request.getValue());
                case "DELETE" -> response = DATABASE.delete(request.getKey());
                case "EXIT" -> response = "OK";
            }

            // Send to output
            output.writeUTF(response);

            // Print to console what was sent
            System.out.println("Sent: " + response);

            // Also close server if exit
            if (request.getType().equalsIgnoreCase("EXIT")) {
                Main.server.stop();
            }

        } catch (IOException e) {
            throw new RuntimeException("Server stopped");
        }
    }

}
