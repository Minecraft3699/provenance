package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.cpm.ProvCPM;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.mc3699.provenance.util.ProvScheduler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbilityExecutor {

    public static void runAbility(TriggerAbilityPayload payload, IPayloadContext context)
    {
        ServerPlayer serverPlayer = (ServerPlayer) context.player();
        int slot = payload.abilitySlot();
        BaseAbility ability = ProvenanceDataHandler.getAbility(serverPlayer, slot);
        if(ability != null
                && ability.canExecute(serverPlayer)
                && ability.getUseCost() < ProvenanceDataHandler.getAP(serverPlayer)
                && !ProvenanceDataHandler.isOnCooldown(serverPlayer, slot))
        {
            ability.execute(serverPlayer);
            ProvenanceDataHandler.setCooldown(serverPlayer, slot, ability.getCooldown());


            if(ability.getAnimation() != null)
            {
                ProvScheduler.schedule(1, () ->
                        ProvCPM.serverAPI.playAnimation(Player.class,  serverPlayer, "provenance-"+ability.getAnimation(), 0));
                ProvScheduler.schedule(2, () ->
                        ProvCPM.serverAPI.playAnimation(Player.class,  serverPlayer, "provenance-"+ability.getAnimation(), 1));
            }
        }
    }

}
