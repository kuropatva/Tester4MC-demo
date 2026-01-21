package pl.kuropatva.util;

public class UniqueNameGenerator {

    private static long num = 0;

    public static String get() {
        return Settings.get(Settings.Field.NAME_SERVER_PREFIX) + num++;
    }

}
