package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.mc3699.provenance.network.RequestDataSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber
public class PlayerSyncHandler {

    @SubscribeEvent
    public static void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            CompoundTag abilities = serverPlayer.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
            if (abilities.isEmpty()) {
                abilities = new CompoundTag();
            }
            PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(abilities));
        }
    }

    public static void handleSyncRequests(RequestDataSyncPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            CompoundTag abilities = serverPlayer.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
            if (abilities.isEmpty()) {
                abilities = new CompoundTag();
            }
            PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(abilities.copy()));
        }
    }
}
