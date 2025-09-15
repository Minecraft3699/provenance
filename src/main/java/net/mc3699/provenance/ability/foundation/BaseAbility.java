package net.mc3699.provenance.ability.foundation;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public abstract class BaseAbility {

    public abstract float getUseCost();

    public abstract Component getName();

    public void execute(ServerPlayer player)
    {
        ProvenanceDataHandler.changeAP(player, -getUseCost());
    }

    public abstract boolean canExecute(ServerPlayer player);

    public ResourceLocation getIcon()
    {
        return ProvConstants.path("textures/ability/missing.png");
    }

}
