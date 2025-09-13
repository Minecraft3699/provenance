package net.mc3699.provenance.ability;

import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AbilityExecutor {

    public static void runAbility(TriggerAbilityPayload payload, IPayloadContext context)
    {
        ServerPlayer serverPlayer = (ServerPlayer) context.player();
        int slot = payload.abilitySlot();

        ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
        BaseAbility ability = AbilityDataHandler.getAbility(serverPlayer, slot);
        if(ability != null && ability.canExecute(serverPlayer, serverLevel))
        {
            ability.execute(serverPlayer);
        }
    }

}
