package net.mc3699.provenance.network;

import io.netty.buffer.ByteBuf;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TriggerAbilityPayload(int abilitySlot, boolean state) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TriggerAbilityPayload> TYPE =
            new CustomPacketPayload.Type<>(ProvConstants.path("trigger_ability"));

    public static final StreamCodec<ByteBuf, TriggerAbilityPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            TriggerAbilityPayload::abilitySlot,
            ByteBufCodecs.BOOL,
            TriggerAbilityPayload::state,
            TriggerAbilityPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
