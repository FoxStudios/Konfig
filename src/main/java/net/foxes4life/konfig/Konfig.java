package net.foxes4life.konfig;

import com.google.gson.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class Konfig {
    String configPath;
    String configID;

    JsonObject jsonData;

    public Konfig(String id) {
        configID = id;
        configPath = "config/" + id;
    }

    public Konfig(String id, String path) {
        configID = id;
        configPath = path;
    }

    public void load() {
        try {
            jsonData = JsonParser.parseString(Files.readString(Path.of(configPath))).getAsJsonObject();
        } catch (Exception ex) {
            System.out.println("No config file found for id " + configID + "! Creating new config...");
            jsonData = new JsonObject();
        }
    }

    public void save() {

    }
}
