package server;

import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Database {

    private final Path DB_PATH = Path.of("src/server/data/db.json");
    private JsonObject database;

    public void init() throws IOException {
        if (Files.exists(DB_PATH)) {
            String content = new String(Files.readAllBytes(DB_PATH));
            database = new Gson().fromJson(content, JsonObject.class);
        } else {
            Files.createFile(DB_PATH);
            database = new JsonObject();
            writeToDatabase();
        }
    }

    public String set(JsonElement key, JsonElement value) {
        if (key.isJsonPrimitive()) {
            database.add(key.getAsString(), value);
        } else if (key.isJsonArray()) {
            JsonArray keys = key.getAsJsonArray();
            String toAdd = keys.remove(keys.size() - 1).getAsString();
            findElement(keys, true).getAsJsonObject().add(toAdd, value);
        } else {
            throw new RuntimeException("No such key found");
        }
        writeToDatabase();
        return successResponse();
    }

    public String get(JsonElement key) {
        if (key.isJsonPrimitive() && database.has(key.getAsString())) {
            return getResponseWithValue(database.get(key.getAsString()));
        } else if (key.isJsonArray()) {
            return getResponseWithValue(findElement(key.getAsJsonArray(), false));
        }
        throw new RuntimeException("No such key found");
    }

    public String delete(JsonElement key) {
        if (key.isJsonPrimitive() && database.has(key.getAsString())) {
            database.remove(key.getAsString());
        } else if (key.isJsonArray()) {
            JsonArray keys = key.getAsJsonArray();
            String toRemove = keys.remove(keys.size() - 1).getAsString();
            findElement(keys, false).getAsJsonObject().remove(toRemove);
            writeToDatabase();
            return successResponse();
        } else {
            throw new RuntimeException("No such key found");
        }

        return "ERROR";

    }

    private JsonElement findElement(JsonArray keys, boolean createIfAbsent) {
        JsonElement tmp = database;
        if (createIfAbsent) {
            for (JsonElement key: keys) {
                if (!tmp.getAsJsonObject().has(key.getAsString())) {
                    tmp.getAsJsonObject().add(key.getAsString(), new JsonObject());
                }
                tmp = tmp.getAsJsonObject().get(key.getAsString());
            }
        } else {
            for (JsonElement key: keys) {
                if (!key.isJsonPrimitive() || !tmp.getAsJsonObject().has(key.getAsString())) {
                    throw new RuntimeException("No such key is found!");
                }
                tmp = tmp.getAsJsonObject().get(key.getAsString());
            }
        }
        return tmp;
    }

    private void writeToDatabase() {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(DB_PATH.toString())) {
            writer.write(gson.toJson(database));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String successResponse() {
        JsonObject json = new JsonObject();
        json.addProperty("response", "OK");
        return json.toString();
    }

    private String getResponseWithValue(JsonElement value) {

        Gson gson = new Gson();

        JsonObject json = new JsonObject();
        json.addProperty("response", "OK");
        json.add("value", value);

        return gson.toJson(json);
    }
}
