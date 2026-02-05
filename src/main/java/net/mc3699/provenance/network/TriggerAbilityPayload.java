package net.mc3699.provenance.network;

import io.netty.buffer.ByteBuf;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record TriggerAbilityPayload(ResourceLocation abilityId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TriggerAbilityPayload> TYPE =
            new CustomPacketPayload.Type<>(ProvConstants.path("trigger_ability"));

    public static final StreamCodec<ByteBuf, TriggerAbilityPayload> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            TriggerAbilityPayload::abilityId,
            TriggerAbilityPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
