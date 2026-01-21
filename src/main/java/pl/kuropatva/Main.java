package pl.kuropatva;

import pl.kuropatva.client.simpleClients.CommandTextLoggerClient;
import pl.kuropatva.testing.Timeout;
import pl.kuropatva.testing.ServerTest;
import pl.kuropatva.util.IPGenerator;
import pl.kuropatva.util.InstanceConfig;
import pl.kuropatva.util.Settings;

import java.util.concurrent.TimeUnit;

public class Main {

    private static Timeout defTimeout = new Timeout(20, 10, TimeUnit.SECONDS);

    public static void main(String[] args) {
        exampleTest();
    }

    public static void exampleTest() {
        // beforeOnce
        Settings.set(Settings.Field.DEF_MAIN_DIR, "F:\\IdeaProjects\\");
        IPGenerator.create("test", "127.0.0.1", 25565, 25570);
        //before Each
        var config = InstanceConfig.of("test", "F:\\IdeaProjects\\aaMCServer");
        var server = new ServerTest(config);
        server.start();

        try {
            // test body
            var client1 = CommandTextLoggerClient.of(config, "test", "me 123123123132");
            var result1 = client1.addTest(defTimeout, s -> s.contains("123123123132"));
            server.addSimpleClient(client1);

            var client2 = CommandTextLoggerClient.of(config, "test2", "me 123123123132");
            var result2 = client2.addTest(defTimeout, s -> s.contains("abc"));
            server.addSimpleClient(client2);

            server.joinClients();
            System.out.println(result1.get().value() ? "pass" : "fail");
            System.out.println(result2.get().value() ? "pass" : "fail");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // after Each
            server.delete();
            IPGenerator.free("test", config);
        }
    }


}