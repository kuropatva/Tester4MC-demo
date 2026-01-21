package pl.kuropatva.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

// Might need to be rewritten someday, but atm I feel like it's enough

public class Settings {

    private static final Map<Field, String> map = Collections.synchronizedMap(new EnumMap<>(Field.class));

    static {
        for (Field field : Field.values()) {
            map.put(field, field.def);
        }
    }

    public static void set(Field field, String value) {
        map.put(field, value);
    }

    public static String get(Field field) {
        return map.get(field);
    }

    public static boolean getBoolean(Field field) {
        return Boolean.parseBoolean(get(field));
    }

    public static int getInt(Field field) {
        try {
            return Integer.parseInt(get(field));
        } catch (NumberFormatException e) {
            return Integer.parseInt(field.def);
        }
    }

    public enum Field {
        DEF_MAIN_DIR(""),
        NAME_SERVER_PREFIX("mcserver_test"),
        GENERATE_SERVER_PROPERTIES("true"),
        SERVER_PROPERTIES_NAME("server.properties"),
        SERVER_ONLINE_CHECKER_TIMEOUT("1000");

        Field(String def) {
            this.def = def;
        }

        private final String def;
    }
}
