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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Konfig {
    String configPath;
    String configID;

    JsonObject jsonData;
    HashMap<String, KonfigCategory> data = new HashMap<>();

    public Konfig(String id) {
        configID = id;
        configPath = "config/" + id;
        load();
    }

    public Konfig(String id, String path) {
        configID = id;
        configPath = path;
        load();
    }

    public void load() {
        try {
            jsonData = JsonParser.parseString(Files.readString(Path.of(configPath + "/config.json"))).getAsJsonObject();
        } catch (IOException ex) {
            System.out.println("Could not loud config file for id " + configID + "! Creating new config...");
            jsonData = new JsonObject();
        }
    }

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

    public Object get(String cat, String entry) {
        try {
            return data.get(cat).catData.get(entry).value;
        } catch (Exception ex) {
            return null;
        }
    }

    public HashMap<String, KonfigCategory> getData() {
        return data;
    }
}
