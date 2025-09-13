package net.mc3699.provenance.ability;

import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class BaseAbility {

    public float getUseCost()
    {
        return 1f;
    }

    public void execute(ServerPlayer player)
    {

    }

    public boolean canExecute(ServerPlayer player, ServerLevel serverLevel)
    {
        return true;
    }

    public ResourceLocation getIcon()
    {
        return ProvConstants.path("textures/ability/missing.png");
    }

}
