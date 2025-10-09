package net.mc3699.provenance.ability.abilities;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
        super.execute(player);
        player.sendSystemMessage(Component.literal("Triggered Test Ability"));
    }

    @Override
    public int getCooldown() {
        return 40;
    }

    @Override
    public String getAnimation() {
        return "test";
    }
}
