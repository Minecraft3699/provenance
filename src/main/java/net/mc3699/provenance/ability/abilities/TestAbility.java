package net.mc3699.provenance.ability.abilities;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.network.OpenScreenPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.function.Supplier;

public class TestAbility extends BaseAbility {
    @Override
    public float getUseCost() {
        return 0;
    }

    @Override
    public Component getName() {
        return Component.literal("Test Ability");
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }

    @Override
    public void execute(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Triggered Test Ability"));
    }

    @Override
    public int getCooldown() {
        return 40;
    }
}
