package pl.kuropatva;

import pl.kuropatva.client.simpleClients.CommandTextLoggerClient;
import pl.kuropatva.testing.Timeout;
import pl.kuropatva.testing.ServerTest;
import pl.kuropatva.util.IPGenerator;
import pl.kuropatva.util.InstanceConfig;
import pl.kuropatva.util.Settings;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/* Example code in PlayerPointsTests class */

public class Main {

//     static void main(String[] args) {
//        joinClient("PlayerBlue");
//
//    }

    public static void joinClient(String nick) {
        var config = new InstanceConfig(new InetSocketAddress("127.0.0.1", 25565), Path.of(""), Path.of(""));
        var client = CommandTextLoggerClient.of(config, nick, "me hi");
        client.join();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.kill();
    }

}