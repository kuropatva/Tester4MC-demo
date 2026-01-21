package pl.kuropatva.client.simpleClients;

import pl.kuropatva.client.AdapterClient;
import pl.kuropatva.client.MCClient;
import pl.kuropatva.testing.FutureResult;
import pl.kuropatva.testing.Timeout;
import pl.kuropatva.util.InstanceConfig;

import java.util.function.Function;

public class CommandTextLoggerClient implements MCClient {

    private final AdapterClient client;
    private final TextLoggerAdapter textLoggerAdapter = new TextLoggerAdapter();
    private String[] commands;

    public CommandTextLoggerClient(InstanceConfig config, String name) {
        this.client = AdapterClient.of(config, name);
    }

    public static CommandTextLoggerClient of(InstanceConfig config, String name, String... commands) {
        var client = new CommandTextLoggerClient(config, name);
        client.setCommands(commands);
        return client;
    }

    public FutureResult addTest(Timeout timeout, Function<String, Boolean> condition) {
        return textLoggerAdapter.getLogger().testFor(timeout, condition);
    }

    public void setCommands(String... commands) {
        this.commands = commands;
    }

    @Override
    public void join() {
        client.addAdapter(textLoggerAdapter);
        client.addAdapter(new CommandAdapter(commands));
        client.join();
    }

    @Override
    public void kill() {
        client.kill();
    }
}
