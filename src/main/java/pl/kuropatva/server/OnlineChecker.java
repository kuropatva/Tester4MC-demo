package pl.kuropatva.server;

import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;

import pl.kuropatva.util.InstanceConfig;
import pl.kuropatva.util.Settings;

import java.net.Socket;

public class OnlineChecker {

    private MinecraftProtocol protocol;
    private InstanceConfig config;

    public OnlineChecker(InstanceConfig instanceConfig) {
        this.config = instanceConfig;
    }


    public boolean isOnline() {
        boolean result;
        try (Socket socket = new Socket()) {
            socket.connect(config.ip(), Settings.getInt(Settings.Field.SERVER_ONLINE_CHECKER_TIMEOUT));
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
