package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.base.BaseAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TestAbility extends BaseAbility {
    @Override
    public float getUseCost() {
        return 10;
    }

    @Override
    public String getName() {
        return "Test Ability";
    }

    @Override
    public void execute(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("IT WORKS"));
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }
}
