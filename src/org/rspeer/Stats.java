package org.rspeer;

import java.util.HashMap;

public class Stats {

    public static HashMap<String, Integer> killed = new HashMap<>();

    public static void onKilled(String name) {
        if(killed.containsKey(name)) {
            killed.put(name, killed.get(name) + 1);
        } else {
            killed.put(name, 1);
        }
    }

    public static HashMap<String, Integer> getKilled() {
        return killed;
    }
}
