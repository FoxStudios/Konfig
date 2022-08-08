package net.foxes4life.konfig;

import com.google.gson.*;
import net.foxes4life.konfig.data.KonfigCategory;
import net.foxes4life.konfig.data.KonfigEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Konfig {
    String configPath;
    String configID;

    JsonObject jsonData;
    LinkedHashMap<String, KonfigCategory> data = new LinkedHashMap<>();

    /**
     * Creates a new Konfig instance.
     * @param id Konfig instance id
     */
    public Konfig(String id) {
        configID = id;
        configPath = "config/" + id;
        load();
    }

    /**
     * Creates a new Konfig instance with a custom path.
     * @param id Konfig instance id
     * @param path The custom folder path
     */
    public Konfig(String id, String path) {
        configID = id;
        configPath = path;
        load();
    }

    /**
     * Tries loading the config json file from the disk. (Use after adding all categories!)
     */
    public void load() {
        try {
            jsonData = JsonParser.parseString(Files.readString(Path.of(configPath + "/config.json"))).getAsJsonObject();
        } catch (IOException ex) {
            System.out.println("Could not loud config file for id " + configID + "! Creating new config...");
            jsonData = new JsonObject();
        }
    }

    /**
     * Saves the data to the disk.
     * @throws IOException
     */
    public void save() throws IOException {
        for (Map.Entry<String, KonfigCategory> category : data.entrySet()) {
            JsonObject catjsondata = new JsonObject();
            for (Map.Entry<String, KonfigEntry> entry : category.getValue().catData.entrySet()) {
                if(entry.getValue().value instanceof String) { // this is probably still dumb
                    catjsondata.addProperty(entry.getKey(), (String) entry.getValue().value);
                } else if(entry.getValue().value instanceof Number) {
                    catjsondata.addProperty(entry.getKey(), (Number) entry.getValue().value);
                } else if(entry.getValue().value instanceof Boolean) {
                    catjsondata.addProperty(entry.getKey(), (Boolean) entry.getValue().value);
                }
            }
            jsonData.add(category.getKey(), catjsondata);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File savePath = new File(configPath);
        if(!savePath.exists()) {
            if(!savePath.mkdirs()) {
                throw new IOException("Could not create directory!");
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(configPath, "config.json")));
        writer.write(gson.toJson(jsonData));
        writer.close();
    }

    /**
     * Adds a new category for entries.
     * @param cat The category to be added
     */
    public void addCategory(KonfigCategory cat) {
        JsonElement catjsondata = jsonData.get(cat.catName);
        System.out.println("[Konfig " + configID + "] Loading category " + cat.catName + " with data " + catjsondata);

        if (catjsondata == null) {
            cat.catJsonData = new JsonObject();
        } else {
            cat.catJsonData = catjsondata.getAsJsonObject();
        }

        cat.loadStuff();
        data.put(cat.catName, cat);
    }

    /**
     * Changes the value of an entry.
     * @param cat Category name
     * @param entry Entry name
     * @param value The new value
     */
    public void set(String cat, String entry, Object value) {
        try {
            Object val = data.get(cat).catData.get(entry).value;
            boolean isSameType = false;
            if(val instanceof String) {
                if(value instanceof String) isSameType = true;
            } else if(val instanceof Number) {
                if(value instanceof Number) isSameType = true;
            } else if(val instanceof Boolean) {
                if(value instanceof Boolean) isSameType = true;
            }
            if (!isSameType)
                throw new Exception("Not the same type");

            data.get(cat).catData.get(entry).value = value;
        } catch (Exception ex) {
            System.out.println("[Konfig "+ configID + "] Couldnt set " + cat + "." + entry + " as " + value);
        }
    }

    /**
     * Returns a value from an entry.
     * @param cat Category name
     * @param entry Entry name
     * @return The requested entry value (Returns null if not found)
     */
    public Object get(String cat, String entry) {
        try {
            return data.get(cat).catData.get(entry).value;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Returns all categories and entries in a Map.
     * @return The data
     */
    public LinkedHashMap<String, KonfigCategory> getData() {
        return data;
    }
}
