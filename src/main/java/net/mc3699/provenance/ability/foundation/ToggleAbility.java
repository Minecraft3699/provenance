package net.mc3699.provenance.ability.foundation;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.minecraft.server.level.ServerPlayer;

public abstract class ToggleAbility extends BaseAbility {

    private boolean enabled = false;

    public void tick(ServerPlayer serverPlayer) {
        ProvenanceDataHandler.changeAP(serverPlayer, -getUseCost());
        if (ProvenanceDataHandler.getAP(serverPlayer) < getUseCost()) enabled = false;
    }

    public void backgroundTick(ServerPlayer serverPlayer) {
    }

    public boolean isEnabled(ServerPlayer player, int slot) {
        return ProvenanceDataHandler.isAbilityEnabled(player, slot);
    }

    public void setEnabled(ServerPlayer player, int slot, boolean enabled) {
        ProvenanceDataHandler.setAbilityEnabled(player, slot, enabled);
    }


}
