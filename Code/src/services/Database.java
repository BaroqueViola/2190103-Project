package src.services;

import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Database instance;
    private final Map<String, Object> memory = new HashMap<>();

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // --- Core Functions ---

    public void set(String key, Object value) {
        memory.put(key, value);
    }

    public Object get(String key) {
        return memory.get(key);
    }

    public String getString(String key) {
        Object value = memory.get(key);
        return value != null ? value.toString() : "";
    }

    public double getDouble(String key) {
        Object value = memory.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value != null) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = memory.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }

    public boolean contains(String key) {
        return memory.containsKey(key);
    }

    public void clear() {
        memory.clear();
    }
}
