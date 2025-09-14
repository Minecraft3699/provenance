package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientAbilityInfo {
    public static CompoundTag clientData;

    public static void handle(AbilityDataSyncPayload payload, IPayloadContext context)
    {
        clientData = payload.abilities();
    }
}
