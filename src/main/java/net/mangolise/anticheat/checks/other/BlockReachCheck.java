package net.mangolise.anticheat.checks.other;

import net.mangolise.anticheat.ACCheck;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import org.jetbrains.annotations.NotNull;

public class BlockReachCheck extends ACCheck {

    public BlockReachCheck() {
        super("BlockReach");
    }

    @Override
    public void register(EventNode<EntityEvent> events) {
        events.addListener(PlayerPacketEvent.class, this::playerPacket);
    }

    private void playerPacket(@NotNull PlayerPacketEvent e) {
        if (!(e.getPacket() instanceof ClientPlayerDiggingPacket packet)) return;
        if (packet.status() != ClientPlayerDiggingPacket.Status.STARTED_DIGGING) return;
        Player player = e.getPlayer();

        if (isBypassing(player)) return;

        double distance = player.getPosition().distance(packet.blockPosition());
        debug(player, "Distance: " + distance);

        if (distance > player.getAttributeValue(Attribute.BLOCK_INTERACTION_RANGE) + 0.9) {
            float certainty = Math.min(1f, ((float) distance / 10f) + 0.7f);
            flag(player, certainty);
        }
    }
}
