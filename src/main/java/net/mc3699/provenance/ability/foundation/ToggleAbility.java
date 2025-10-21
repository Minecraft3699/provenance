package net.mc3699.provenance.ability.foundation;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public abstract class ToggleAbility extends BaseAbility {

    private boolean enabled = false;

    public void tick(ServerPlayer serverPlayer)
    {
        ProvenanceDataHandler.changeAP(serverPlayer, -getUseCost());
        if(ProvenanceDataHandler.getAP(serverPlayer) < getUseCost()) enabled = false;
    }

    public abstract void backgroundTick(ServerPlayer serverPlayer);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean val)
    {
        this.enabled = val;
    }

    @Override
    public void execute(ServerPlayer player) {
        enabled = !enabled;
    }


}
