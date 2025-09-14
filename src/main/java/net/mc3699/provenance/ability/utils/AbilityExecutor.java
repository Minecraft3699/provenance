package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.ability.base.BaseAbility;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbilityExecutor {

    public static void runAbility(TriggerAbilityPayload payload, IPayloadContext context)
    {
        ServerPlayer serverPlayer = (ServerPlayer) context.player();
        int slot = payload.abilitySlot();
        BaseAbility ability = AbilityDataHandler.getAbility(serverPlayer, slot);
        if(ability != null && ability.canExecute(serverPlayer) && ability.getUseCost() < AbilityDataHandler.getAP(serverPlayer))
        {
            ability.execute(serverPlayer);
        }
    }

}
