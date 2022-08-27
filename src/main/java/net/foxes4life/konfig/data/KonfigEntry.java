package net.foxes4life.konfig.data;

import com.google.gson.JsonElement;

public class KonfigEntry {
    Object value;
    Object defaultValue;

    String entryName;
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
            if (defaultValue instanceof String) {
                value = jsonValue.getAsString();
            } else if (defaultValue instanceof Number) {
                value = jsonValue.getAsNumber();
            } else if (defaultValue instanceof Boolean) {
                value = jsonValue.getAsBoolean();
            }
        }
    }

    public boolean isSameType(Object val) {
        return value.getClass() == val.getClass();
    }

    public boolean isString() {
        return value instanceof String;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public String getAsString() {
        return isString() ? (String) value : null;
    }

    public Number getAsNumber() {
        return isNumber() ? (Number) value : null;
    }

    public boolean getAsBoolean() {
        return isBoolean() ? (Boolean) value : null;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String getEntryName() {
        return entryName;
    }
}
