package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.cpm.ProvCPM;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.mc3699.provenance.util.ProvScheduler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbilityExecutor {

    public static void runAbility(TriggerAbilityPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        int slot = payload.abilitySlot();
        BaseAbility ability = ProvenanceDataHandler.getAbility(player, slot);
        if (ability == null || !ability.canExecute(player)) return;

        if (ability instanceof ToggleAbility toggle) {
            boolean currentlyEnabled = toggle.isEnabled(player, slot);
            toggle.setEnabled(player, slot, !currentlyEnabled);

            if (!currentlyEnabled && toggle.getUseCost() <= ProvenanceDataHandler.getAP(player)) {
                toggle.tick(player);
            }
        } else {
            if (ability.getUseCost() <= ProvenanceDataHandler.getAP(player) && !ProvenanceDataHandler.isOnCooldown(player, slot)) {

                ability.execute(player);
                if (ability.getAnimation() != null) {
                    ProvScheduler.schedule(1, () -> ProvCPM.serverAPI.playAnimation(Player.class, player, "provenance-" + ability.getAnimation(), 0));
                    ProvScheduler.schedule(2, () -> ProvCPM.serverAPI.playAnimation(Player.class, player, "provenance-" + ability.getAnimation(), 1));
                }
                ProvenanceDataHandler.setCooldown(player, slot, ability.getCooldown());
            }
        }


    }
}
