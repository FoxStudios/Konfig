package net.foxes4life.konfig;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.foxes4life.konfig.bindables.Bindable;
import net.foxes4life.konfig.bindables.BindableNumber;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked") // :shut:
public class Konfig<TLookUp extends Enum> {
    private final Path path;
    private final LinkedHashMap<TLookUp, Bindable> data = new LinkedHashMap<>();

    public boolean verbose = false;

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
        Gson gson = new Gson();
        JsonObject jsonData = new JsonObject();

        if (path.toFile().exists()) {
            try {
                jsonData = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
            } catch (IOException ignored) {}
        }

        for (Map.Entry<TLookUp, Bindable> entry : data.entrySet()) {
            Bindable bindable = entry.getValue();

            if (jsonData.has(entry.getKey().name()))
                bindable.setValue(gson.fromJson(jsonData.get(entry.getKey().name()).toString(), bindable.getValue().getClass()));

            addBind(entry.getKey(), bindable);
        }
    }

    public void save() {
        Gson gson = new Gson();
        JsonObject jsonData = new JsonObject();

        for (Map.Entry<TLookUp, Bindable> entry : data.entrySet()) {
            JsonElement jsonValue = gson.toJsonTree(entry.getValue().getValue());
            jsonData.add(entry.getKey().name(), jsonValue);
        }

        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, jsonData.toString());
        } catch (IOException ignored) {}
    }

    public void setDefault(TLookUp lookup, double value, double min, double max) {
        BindableNumber<Double> bindable = new BindableNumber<>(value);
        bindable.setMin(min);
        bindable.setMax(max);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, double value) {
        BindableNumber<Double> bindable = new BindableNumber<>(value);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, int value, int min, int max) {
        BindableNumber<Integer> bindable = new BindableNumber<>(value);
        bindable.setMin(min);
        bindable.setMax(max);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, int value) {
        BindableNumber<Integer> bindable = new BindableNumber<>(value);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, float value, float min, float max) {
        BindableNumber<Float> bindable = new BindableNumber<>(value);
        bindable.setMin(min);
        bindable.setMax(max);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, float value) {
        BindableNumber<Float> bindable = new BindableNumber<>(value);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, long value, long min, long max) {
        BindableNumber<Long> bindable = new BindableNumber<>(value);
        bindable.setMin(min);
        bindable.setMax(max);
        addBind(lookup, bindable);
    }

    public void setDefault(TLookUp lookup, long value) {
        BindableNumber<Long> bindable = new BindableNumber<>(value);
        addBind(lookup, bindable);
    }

    public <T> void setDefault(TLookUp lookup, T value) {
        Bindable<T> bindable = new Bindable<>(value);
        addBind(lookup, bindable);
    }

    public <T> T get(TLookUp lookup, Class<T> type) {
        try {
            return type.cast(data.get(lookup).getValue());
        } catch (Exception e) {
            if (verbose) e.printStackTrace();
            return null;
        }
    }

    public <T> Bindable<T> getBind(TLookUp lookup, Class<T> type) {
        try {
            return (Bindable<T>) data.get(lookup);
        } catch (Exception e) {
            if (verbose) e.printStackTrace();
            return null;
        }
    }

    public <T> void set(TLookUp lookup, T value) {
        Bindable<T> bind = getBind(lookup, (Class<T>) value.getClass());

        if (bind == null)
            setDefault(lookup, value);
        else
            bind.setValue(value);
    }

    private <T> void addBind(TLookUp lookup, Bindable<T> bindable) {
        data.put(lookup, bindable);
        bindable.valueChanged.add((newValue) -> save());
    }
}
