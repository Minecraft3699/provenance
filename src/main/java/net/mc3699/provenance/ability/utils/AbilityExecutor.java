package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbilityExecutor {

    public static void runAbility(TriggerAbilityPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        ResourceLocation abilityId = payload.abilityId();

        BaseAbility ability = ProvenanceDataHandler.getAbility(player, abilityId);
        if (ability == null || !ability.canExecute(player) || !ProvenanceDataHandler.getAbilities(player).contains(ability)) return;

        if (ability instanceof ToggleAbility toggle) {
            boolean enabled = ProvenanceDataHandler.isAbilityEnabled(player, abilityId);
            ProvenanceDataHandler.setAbilityEnabled(player, abilityId, !enabled);

            if (!enabled && toggle.getUseCost() <= ProvenanceDataHandler.getAP(player)) {
                toggle.tick(player);
            }
        } else {
            if (ability.getUseCost() <= ProvenanceDataHandler.getAP(player)
                    && !ProvenanceDataHandler.isOnCooldown(player, abilityId)) {

                ability.execute(player);
                ProvenanceDataHandler.setCooldown(player, abilityId, ability.getCooldown());
            }
        }
    }
}
