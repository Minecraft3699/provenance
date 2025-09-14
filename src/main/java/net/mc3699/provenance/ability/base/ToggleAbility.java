package net.mc3699.provenance.ability.base;

import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.mc3699.provenance.handlers.APTickHandler;
import net.minecraft.server.level.ServerPlayer;

public class ToggleAbility extends BaseAbility {

    private boolean enabled = false;

    public void tick(ServerPlayer serverPlayer)
    {
        AbilityDataHandler.changeAP(serverPlayer, -getUseCost());
        if(AbilityDataHandler.getAP(serverPlayer) < getUseCost()) enabled = false;
    }

    public boolean isValid(ServerPlayer serverPlayer)
    {
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void execute(ServerPlayer player) {
        enabled = !enabled;
    }
}
