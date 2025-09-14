package net.mc3699.provenance.network;

import io.netty.buffer.ByteBuf;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RequestDataSyncPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<RequestDataSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(ProvConstants.path("ability_sync_request"));

    public static final StreamCodec<ByteBuf, RequestDataSyncPayload> CODEC =
            StreamCodec.unit(new RequestDataSyncPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
