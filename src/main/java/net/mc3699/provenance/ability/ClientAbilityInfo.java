package net.mc3699.provenance.ability;

import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientAbilityInfo {
    public static CompoundTag clientData;

    public static void handle(AbilityDataSyncPayload payload, IPayloadContext context)
    {
        clientData = payload.abilities();
    }
}
