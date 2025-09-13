package net.mc3699.provenance.network;

import io.netty.buffer.ByteBuf;
import net.mc3699.provenance.ability.BaseAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.ArrayList;

public record AbilityDataSyncPayload(CompoundTag abilities) implements CustomPacketPayload {


    public static final CustomPacketPayload.Type<AbilityDataSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ProvConstants.path("ability_sync"));

    public static final StreamCodec<ByteBuf, AbilityDataSyncPayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.COMPOUND_TAG,
                    AbilityDataSyncPayload::abilities,
                    AbilityDataSyncPayload::new
            );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
