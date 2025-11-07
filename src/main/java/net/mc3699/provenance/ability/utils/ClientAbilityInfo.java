package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientAbilityInfo {

    public static CompoundTag clientData = new CompoundTag();

    public static void handle(AbilityDataSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            CompoundTag incoming = payload.abilities();
            if (incoming != null) {
                clientData = incoming.copy();
            }
        });
    }
}
