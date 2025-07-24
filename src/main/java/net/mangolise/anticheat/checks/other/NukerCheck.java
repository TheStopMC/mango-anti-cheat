package net.mangolise.anticheat.checks.other;

import net.mangolise.anticheat.ACCheck;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;

public class NukerCheck extends ACCheck {

    private static final Tag<Integer> DIG_COUNT_TAG = Tag.Integer("anticheat_nuker_dig_count").defaultValue(0);
    private static final int DIG_THRESHOLD = 6; // tune this based on legit gameplay

    public NukerCheck() {
        super("Nuker");
    }

    @Override
    public void register(EventNode<EntityEvent> events) {
        events.addListener(PlayerPacketEvent.class, this::onDigPacket);
    }

    private void onDigPacket(PlayerPacketEvent event) {
        if (!(event.getPacket() instanceof ClientPlayerDiggingPacket dig)) return;
        if (dig.status() != ClientPlayerDiggingPacket.Status.STARTED_DIGGING) return;

        Player player = event.getPlayer();
        if (isBypassing(player)) return;

        int newCount = player.getTag(DIG_COUNT_TAG) + 1;
        player.setTag(DIG_COUNT_TAG, newCount);

        // Schedule reset if this is the first dig this tick
        if (newCount == 1) {
            MinecraftServer.getSchedulerManager().buildTask(() ->
                    player.setTag(DIG_COUNT_TAG, 0)
            ).delay(TaskSchedule.nextTick()).schedule();
        }

        // Flag if they exceeded the threshold
        if (newCount > DIG_THRESHOLD) {
            flag(player, 0.95f);
            debug(player, "Nuker detected: " + newCount + " dig packets in one tick");
        }
    }
}
