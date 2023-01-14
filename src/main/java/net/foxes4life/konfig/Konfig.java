package net.foxes4life.konfig;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Konfig<TLookUp extends Enum> {
    private final Path path;
    private final LinkedHashMap<TLookUp, Object> data = new LinkedHashMap<>();
    private final LinkedHashMap<TLookUp, Object> defaults = new LinkedHashMap<>();

    /**
     * Creates a new Konfig instance.
     * Automatically tries loading the config file.
     *
     * @param id Konfig instance id
     */
    public Konfig(String id) {
        this.path = Path.of("config", id, "config.json");
        initializeDefaults();
        load();
    }

    /**
     * Creates a new Konfig instance with a custom path.
     * Automatically tries loading the config file.
     *
     * @param path The custom path
     */
    public Konfig(Path path) {
        this.path = path;
        initializeDefaults();
        load();
    }

    public void initializeDefaults() {
        // Override this method to initialize your defaults
    }

    public void load() {
        JsonObject jsonData = new JsonObject();

        if (path.toFile().exists()) {
            try {
                jsonData = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            } catch (IOException ignored) {}
        }

        for (Map.Entry<TLookUp, Object> entry : defaults.entrySet()) {
            if (jsonData.has(entry.getKey().name())) {
                if (entry.getValue() instanceof Number) {
                    data.put(entry.getKey(), jsonData.get(entry.getKey().name()).getAsNumber());
                } else if (entry.getValue() instanceof Boolean) {
                    data.put(entry.getKey(), jsonData.get(entry.getKey().name()).getAsBoolean());
                } else if (entry.getValue() instanceof String) {
                    data.put(entry.getKey(), jsonData.get(entry.getKey().name()).getAsString());
                } else if (entry.getValue() instanceof JsonElement) {
                    data.put(entry.getKey(), jsonData.get(entry.getKey().name()));
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + entry.getValue().getClass().getName());
                }
            } else {
                data.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void save() {
        JsonObject jsonData = new JsonObject();

        for (Map.Entry<TLookUp, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof Number) {
                jsonData.addProperty(entry.getKey().name(), (Number) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                jsonData.addProperty(entry.getKey().name(), (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                jsonData.addProperty(entry.getKey().name(), (String) entry.getValue());
            } else if (entry.getValue() instanceof JsonElement) {
                jsonData.add(entry.getKey().name(), (JsonElement) entry.getValue());
            } else {
                throw new IllegalArgumentException("Unsupported type: " + entry.getValue().getClass().getName());
            }
        }

        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, jsonData.toString());
        } catch (IOException ignored) {}
    }

    public void setDefault(TLookUp lookup, Object value) {
        defaults.put(lookup, value);
    }

    public <T> T get(TLookUp lookup, Class<T> type) {
        try {
            if (data.containsKey(lookup)) {
                return type.cast(data.get(lookup));
            } else {
                return type.cast(defaults.get(lookup));
            }
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void set(TLookUp lookup, Object value) {
        data.put(lookup, value);
    }
}
