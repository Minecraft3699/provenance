package net.mc3699.provenance.ability.base;

import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class BaseAbility {

    public float getUseCost()
    {
        return 0.25f;
    }

    public String getName()
    {
        return "Ability";
    }

    public int getCooldownTicks()
    {
        return 20;
    }

    public void execute(ServerPlayer player)
    {
        AbilityDataHandler.changeAP(player, -getUseCost());
    }

    public boolean canExecute(ServerPlayer player)
    {
        return true;
    }

    public ResourceLocation getIcon()
    {
        return ProvConstants.path("textures/ability/missing.png");
    }

}
