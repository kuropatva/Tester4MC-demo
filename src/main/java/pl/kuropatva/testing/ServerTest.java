package pl.kuropatva.testing;

import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import pl.kuropatva.client.AdapterClient;
import pl.kuropatva.server.OnlineChecker;
import pl.kuropatva.server.ServerDotPropertiesCreator;
import pl.kuropatva.server.ShellRunner;
import pl.kuropatva.client.MCClient;
import pl.kuropatva.util.DirCopy;
import pl.kuropatva.util.InstanceConfig;
import pl.kuropatva.util.Settings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.onSpinWait;

public class ServerTest {

    private final InstanceConfig instanceConfig;
    private final ArrayList<MCClient> clients = new ArrayList<>();
    private final ShellRunner shellRunner = new ShellRunner();
    private Process process;
    private final OnlineChecker oChecker;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);


    public ServerTest(InstanceConfig instanceConfig)  {
        this.instanceConfig = instanceConfig;
        shellRunner.setDir(instanceConfig.to());
        oChecker = new OnlineChecker(instanceConfig);
    }

    public void addAdapterClient(String name, SessionAdapter listener) {
        var client = AdapterClient.of(instanceConfig, name, listener);
        clients.add(client);
    }

    public void addSimpleClient(MCClient mcClient) {
        clients.add(mcClient);

    }

    public void start() {
        if (isRunning.getAndSet(true)) return;
        cleanServer();
        try {
            process = shellRunner.run();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinClients() {
        if (!isRunning.get()) return;
        while (!oChecker.isOnline()) {
            onSpinWait();
        }
        for (var client : clients) {
            client.join();
        }
    }

    public void delete() {
        if (!isRunning.get()) return;
        for (var client : clients) {
            try {
                client.kill();
            } catch (NullPointerException _) {}
        }
        process.destroyForcibly();
        isRunning.set(false);
    }

    private void cleanServer() {
        try {
            DirCopy.copyDirClean(instanceConfig.from(), instanceConfig.to());
            if (Settings.getBoolean(Settings.Field.GENERATE_SERVER_PROPERTIES)) {
                ServerDotPropertiesCreator.toFile(Path.of(instanceConfig.to() + "/" + Settings.get(Settings.Field.SERVER_PROPERTIES_NAME)), instanceConfig);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
