package targetTests;

import org.junit.jupiter.api.*;
import pl.kuropatva.client.simpleClients.CommandTextLoggerClient;
import pl.kuropatva.server.SystemLocalisedCommands;
import pl.kuropatva.testing.ServerTest;
import pl.kuropatva.testing.Timeout;
import pl.kuropatva.util.IPGenerator;
import pl.kuropatva.util.InstanceConfig;
import pl.kuropatva.util.Settings;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

/*
Demo created for https://github.com/Rosewood-Development/PlayerPoints/
based on default English localisation

FYI: Timeout starts after all players are set to join, not after they actually joined

*/


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PlayerPointsTests {

    private ServerTest server;
    private InstanceConfig config;
    private static final Timeout defTimeout = new Timeout(20, 20, TimeUnit.SECONDS);

    private static final String richPlayer = "PlayerBlue";
    private static final String brokePlayer = "PlayerRed";
    private static final String admin = "Admin";

    @BeforeAll
    public static void init() {
        Settings.set(Settings.Field.DEF_MAIN_DIR, "/Users/michal/Desktop/Tester4MC_DIR/");
//        Settings.set(Settings.Field.DEF_MAIN_DIR, "F:\\Tester4MC_DIR\\");
        IPGenerator.create("test", "127.0.0.1", 25565, 25580);
        SystemLocalisedCommands.Command.RUN.setWindows("java -Xmx2G -jar run.jar nogui");
        SystemLocalisedCommands.Command.RUN.setUnix("java -Xmx2G -jar run.jar nogui");
    }

    @BeforeEach
    public void initTest() {
        config = InstanceConfig.of("test", "/Users/michal/IdeaProjects/spigot121");
//        config = InstanceConfig.of("test", "F:\\spigot121");
        server = new ServerTest(config);
        server.start();
    }

    @AfterEach
    public void close() {
        server.delete();
        IPGenerator.free("test", config);
    }

    @Test
    public void meCheck() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("You have 1,000 Points."));
        var client2 = CommandTextLoggerClient.of(config, brokePlayer, "points me");
        var result2 = client2.addTest(defTimeout, s -> s.contains("You have 0 Points."));
        server.addSimpleClient(client1);
        server.addSimpleClient(client2);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void pay() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + brokePlayer + " 499", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("You paid PlayerRed 499 Points."));
        var result3 = client1.addTest(defTimeout, s -> s.contains("You have 501 Points."));
        var client2 = CommandTextLoggerClient.of(config, brokePlayer, "", "points me");
        var result2 = client2.addTest(defTimeout, s -> s.contains("You have 499 Points."));
        server.addSimpleClient(client1);
        server.addSimpleClient(client2);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
        assertTrue(result3.get().value(), result3.get().cause());
    }

    @Test
    public void payNoMoney() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + brokePlayer + " 1100", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("You do not have enough Points for that."));
        var client2 = CommandTextLoggerClient.of(config, brokePlayer, "points pay " + richPlayer + " 100", "points me");
        var result2 = client2.addTest(defTimeout, s -> s.contains("You do not have enough Points for that."));
        server.addSimpleClient(client1);
        server.addSimpleClient(client2);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void payMinus() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + brokePlayer + " -10", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("Amount must be a positive whole number"));
        var result2 = client1.addTest(defTimeout, s -> s.contains("You have 1,000 Points"));
        server.addSimpleClient(client1);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void payZero() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + brokePlayer + " 0", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("Amount must be a positive whole number"));
        var result2 = client1.addTest(defTimeout, s -> s.contains("You have 1,000 Points"));
        server.addSimpleClient(client1);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void payYourself() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + richPlayer + " 499", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("You cannot pay yourself!"));
        var result2 = client1.addTest(defTimeout, s -> s.contains("You have 1,000 Points"));
        server.addSimpleClient(client1);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void payNoAmount() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points pay " + brokePlayer + "", "points me");
        var result1 = client1.addTest(defTimeout, s -> s.contains("Usage: /points pay PlayerRed <amount>"));
        var result2 = client1.addTest(defTimeout, s -> s.contains("You have 1,000 Points."));
        server.addSimpleClient(client1);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
    }

    @Test
    public void look() {
        var client1 = CommandTextLoggerClient.of(config, richPlayer, "points look " + brokePlayer, "points look " + richPlayer);
        var result1 = client1.addTest(defTimeout, s -> s.contains(brokePlayer + " has 0 Points"));
        var result2 = client1.addTest(defTimeout, s -> s.contains(richPlayer + " has 1,000 Points"));
        var client2 = CommandTextLoggerClient.of(config, brokePlayer, "points look " + richPlayer);
        var result3 = client2.addTest(defTimeout, s -> s.contains(richPlayer + " has 1,000 Points."));
        server.addSimpleClient(client1);
        server.addSimpleClient(client2);
        server.joinClients();
        assertTrue(result1.get().value(), result1.get().cause());
        assertTrue(result2.get().value(), result2.get().cause());
        assertTrue(result3.get().value(), result3.get().cause());
    }

}
