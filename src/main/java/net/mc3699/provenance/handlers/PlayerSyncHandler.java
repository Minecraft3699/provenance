package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.AbilityDataHandler;
import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Provenance.MODID)
public class PlayerSyncHandler {

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if(event.getEntity() instanceof ServerPlayer serverPlayer)
        {
            PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(serverPlayer.getPersistentData().getCompound(AbilityDataHandler.ABILITY_TAG)));
        }

    }

}
