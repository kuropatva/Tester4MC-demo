package pl.kuropatva.util;

import java.net.InetSocketAddress;
import java.nio.file.Path;

public record InstanceConfig(InetSocketAddress ip, Path from, Path to) {

    private static String mainDir = Settings.get(Settings.Field.DEF_MAIN_DIR);

    public static  InstanceConfig of(String portPool, String from) {
        var ip = IPGenerator.acquire(portPool);
        if (ip == null) throw new RuntimeException("No ports available");
        return new InstanceConfig(ip, Path.of(from), Path.of(mainDir + UniqueNameGenerator.get()));
    }

    public static void setMainDir(String dir) {
        mainDir = dir;
    }
}
