package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AmbientTestAbility extends AmbientAbility {
    @Override
    public void tick(ServerPlayer player) {
        if(player.tickCount % 40 == 0) {
            player.sendSystemMessage(Component.literal("Ambient Ability Test"));
        }
    }

    @Override
    public Component getName() {
        return Component.literal("Ambient Test Ability");
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }
}