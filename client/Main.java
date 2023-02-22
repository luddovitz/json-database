package client;

import com.beust.jcommander.JCommander;

public class Main {

    public static void main(String[] args) {

        DbConnection dbConnection = new DbConnection();
        CommandLineArguments commandLineArguments = new CommandLineArguments();

        // Parse cmdl-arguments
        JCommander.newBuilder()
                .addObject(commandLineArguments)
                .build()
                .parse(args);

        dbConnection.setRequest(commandLineArguments);
        dbConnection.connect();

    }
}
