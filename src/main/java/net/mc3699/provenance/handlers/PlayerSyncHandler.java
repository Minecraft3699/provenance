package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.mc3699.provenance.network.RequestDataSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PlayerSyncHandler {

    public static void handleSyncRequests(RequestDataSyncPayload payload, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer serverPlayer)) return;

        CompoundTag abilities = serverPlayer.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);

        PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(abilities.copy()));
    }
}
