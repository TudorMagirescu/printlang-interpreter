package com.printlang.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private final Map<String, Integer> env;

    public Env() {
        this.env = new HashMap<>();
    }

    public Env(Env other) {
        this.env = new HashMap<>(other.env);
    }

    public Integer get(String id) {
        if (!env.containsKey(id)) return null;
        return env.get(id);
    }

    public void put(String id, Integer val) {
        env.put(id, val);
    }
}
