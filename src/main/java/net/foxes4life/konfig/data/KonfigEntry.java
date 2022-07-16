package net.foxes4life.konfig.data;

import com.google.gson.JsonElement;

public class KonfigEntry {
    public Object value;
    Object defaultValue;

    public String entryName;
    KonfigCategory category;

    public KonfigEntry(KonfigCategory cat, String name, Object val) {
        category = cat;
        entryName = name;
        defaultValue = val;
    }

    public void loadValue() {
        JsonElement jsonValue = category.catJsonData.getAsJsonObject().get(entryName);

        if (jsonValue == null) {
            value = defaultValue;
        } else {
            if(defaultValue instanceof String) {
                value = jsonValue.getAsString();
            } else if(defaultValue instanceof Number) {
                value = jsonValue.getAsNumber();
            } else if(defaultValue instanceof Boolean) {
                value = jsonValue.getAsBoolean();
            }
        }
    }
}
