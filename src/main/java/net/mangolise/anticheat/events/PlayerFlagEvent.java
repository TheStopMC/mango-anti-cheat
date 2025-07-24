package net.mangolise.anticheat.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerFlagEvent implements PlayerEvent {
    private final String checkName;
    private final Player player;
    private final float certainty;

    public PlayerFlagEvent(String checkName, Player player, float certainty) {
        this.checkName = checkName;
        this.player = player;
        this.certainty = certainty;
    }

    public String checkName() {
        return checkName;
    }

    public float certainty() {
        return certainty;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
