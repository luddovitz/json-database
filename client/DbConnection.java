package client;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class DbConnection {

    final String ADDRESS = "127.0.0.1";
    final int PORT = 23456;

    Gson gson = new Gson().newBuilder().enableComplexMapKeySerialization().create();
    String request;

    DbConnection() {
        System.out.println("Client started!");
    }

    public void setRequest(CommandLineArguments cmdargs) {

        if (cmdargs.file != null) {
            this.request = requestObjectFromFile(cmdargs.file);
        } else {
            this.request = gson.toJson(cmdargs);
        }
    }

    public void connect() {

        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), (PORT))) {

            // Open IO streams
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // Send request to output
            output.writeUTF(request);

            // Print to console
            System.out.println("Sent: " + request);

            // What is the response from server?
            String response = input.readUTF();

            // Print the response from server
            System.out.println("Received: " + response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Open file by filename, read first line and return as string

    private String requestObjectFromFile(String fileName) {
        try (FileReader fileReader = new FileReader("src/client/data/" + fileName);
            BufferedReader buffReader = new BufferedReader(fileReader)) {
            return JsonParser.parseReader(buffReader).toString();
        } catch (IOException e) {
            throw new RuntimeException("File couldn't be found.");
        }
    }
}
