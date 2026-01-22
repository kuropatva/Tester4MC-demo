package pl.kuropatva.client;

import org.geysermc.mcprotocollib.network.ClientSession;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import pl.kuropatva.util.InstanceConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdapterClient implements MCClient {

    private ClientSession session;
    private Thread connectionThread;
    private final AtomicBoolean connected = new AtomicBoolean(false);

    private InstanceConfig instanceConfig;
    private String username;
    private ArrayList<SessionAdapter> adapters = new ArrayList<>();

    private AdapterClient() {

    }

    public static AdapterClient of(InstanceConfig instanceConfig, String username, SessionAdapter... sessionAdapters) {
        var client = new AdapterClient();
        client.instanceConfig = instanceConfig;
        client.username = username;
        client.adapters.addAll(Arrays.asList(sessionAdapters));
        client.setup();
        return client;
    }

    private void setup() {
        MinecraftProtocol protocol = new MinecraftProtocol(username);

        session = ClientNetworkSessionFactory.factory()
                .setRemoteSocketAddress(instanceConfig.ip())
                .setProtocol(protocol)
                .setProxy(null)
                .create();
//        session.addListener(new SessionAdapter() {
//            @Override
//            public void disconnected(DisconnectedEvent event) {
//                System.out.println("Client disconnected");
//                System.out.println("Reason: " + event.getReason());
//                System.out.println("Cause: " + event.getCause());
//            }
//        });
    }

    public void addAdapter(SessionAdapter adapter) {
        session.addListener(adapter);
    }

    public void removeAdapter(SessionAdapter adapter) {
        session.removeListener(adapter);
    }

    public List<SessionListener> getAdapters() {
        return session.getListeners();
    }

    public void join() {
        if (connected.getAndSet(true)) return;
        for (var adapter : adapters) {
            session.addListener(adapter);
        }
        connectionThread = new Thread(() -> {
            session.connect(true);
        });
        connectionThread.start();
    }


    public void kill() {
        session.disconnect("");
        connectionThread.interrupt();
        connected.set(false);
    }
}
