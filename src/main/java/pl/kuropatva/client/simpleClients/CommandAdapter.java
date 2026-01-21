package pl.kuropatva.client.simpleClients;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.*;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;

public class CommandAdapter extends SessionAdapter {

    private TextLogger logger = new TextLogger();
    private String[] commands;
    private long cmdDelay = 500;

    public CommandAdapter(String... commands) {
        this.commands = commands;
    }

    public TextLogger getLogger() {
        return logger;
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof ClientboundLoginPacket) {
            sendCommands(session);
        }
    }

    private void sendCommands(Session session) {
        for (int i = 0; i < commands.length; i++) {
            session.send(new ServerboundChatCommandPacket(commands[i]));
            if (i == commands.length - 1 ) break;
            try {
                Thread.sleep(cmdDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
