package net.foxes4life.konfig.data;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class KonfigCategory {
    public String catName;
    public HashMap<String, KonfigEntry> catData = new HashMap<>();
    public JsonObject catJsonData;

    public KonfigCategory (String name) {
        catName = name;
    }

    public void addEntry(String name, Object defaultValue) {
        catData.put(name, new KonfigEntry(this, name, defaultValue));
    }

    public void loadStuff() {
        for (Map.Entry<String, KonfigEntry> entry : catData.entrySet()) {
            entry.getValue().loadValue();
        }
    }
}
