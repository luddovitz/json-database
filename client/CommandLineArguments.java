package client;

import com.beust.jcommander.Parameter;
import com.google.gson.annotations.Expose;

public class CommandLineArguments {

    @Expose
    @Parameter(names={"--type", "-t"})
    String type;

    @Expose
    @Parameter(names={"--key", "-k"})
    String key;

    @Expose
    @Parameter(names={"--value", "-v"})
    String value;

    @Expose
    @Parameter(names={"--file", "-in"})
    String file;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFile() {
        return file;
    }
}
