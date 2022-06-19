package net.foxes4life.konfig;

import net.foxes4life.konfig.data.KonfigCategory;

import java.io.IOException;

public class Main {
    /*
     Testing class for the config system
     */
    public static void main(String[] args) throws IOException {
        Konfig konfig = new Konfig("test");

        KonfigCategory test = new KonfigCategory("test");
        test.addEntry("num", 2);
        test.addEntry("bool", false);
        test.addEntry("str", "E");

        konfig.addCategory(test);

        konfig.set("test", "str",  2);

        konfig.save();
    }
}
