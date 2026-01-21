package pl.kuropatva.client.simpleClients;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundDisguisedChatPacket;

public class TextLoggerAdapter extends SessionAdapter {

    private TextLogger logger = new TextLogger();

    public TextLogger getLogger() {
        return logger;
    }

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof ClientboundDisguisedChatPacket systemChatPacket) {
            var message = systemChatPacket.getMessage();
            logger.add(String.valueOf(message));
        }
    }
}
