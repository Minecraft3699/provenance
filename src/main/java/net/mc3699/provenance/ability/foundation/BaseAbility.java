package net.mc3699.provenance.ability.foundation;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public abstract class BaseAbility {

    public abstract float getUseCost();

    public int getCooldown() { return 1;};

    public abstract Component getName();

    public void execute(ServerPlayer player)
    {
        ProvenanceDataHandler.changeAP(player, -getUseCost());
    }

    public abstract boolean canExecute(ServerPlayer player);

    public void backgroundTick(ServerPlayer serverPlayer) {
    }

    public String getAnimation() {
        return null;
    }

    public ResourceLocation getIcon()
    {
        return ProvConstants.path("textures/ability/missing.png");
    }

    public BaseAbility createNew() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
