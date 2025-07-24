package net.mangolise.anticheat.checks.combat;

import net.mangolise.anticheat.ACCheck;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.trait.EntityEvent;

public class ReachCheck extends ACCheck {

    public ReachCheck() {
        super("CombatReach");
    }

    @Override
    public void register(EventNode<EntityEvent> events) {
        events.addListener(EntityAttackEvent.class, e -> {
            if (!(e.getEntity() instanceof Player player)) {
                return;
            }

            if (isBypassing(player)) return;

            double distance = player.getPosition().distance(e.getTarget().getPosition());
            debug(player, "Distance: " + distance);

            if (distance > player.getAttributeValue(Attribute.ENTITY_INTERACTION_RANGE) + 0.9) {
                float certainty = Math.min(1f, ((float) distance / 10f) + 0.7f);
                flag(player, certainty);
            }
        });
    }
}
