package net.mc3699.provenance.ability;

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

    public void canExecute(ServerPlayer player, ServerLevel serverLevel)
    {

    }

}
