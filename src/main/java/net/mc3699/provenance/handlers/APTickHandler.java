package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = Provenance.MODID)
public class APTickHandler {

    @SubscribeEvent
    public static void apTick(ServerTickEvent.Post event)
    {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            AbilityDataHandler.changeAP(player, 0.01f);
            PacketDistributor.sendToPlayer(player,
                    new AbilityDataSyncPayload(player.getPersistentData().getCompound(AbilityDataHandler.ABILITY_TAG)));
        }
    }

}
