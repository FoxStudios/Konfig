package net.foxes4life.konfig;

public class Konfig {
    String configPath;
    String configID;

    public Konfig(String id) {
        configID = id;
        configPath = "config/" + id;
    }

    public Konfig(String id, String path) {
        configID = id;
        configPath = path;
    }

    public void load() {

    }
}
