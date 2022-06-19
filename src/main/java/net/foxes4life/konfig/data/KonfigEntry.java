package net.foxes4life.konfig.data;

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
        value = category.catJsonData.get(entryName);

        if (value == null)
            value = defaultValue;
    }
}
