package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber
public class AmbientAbilityTickHandler {

    @SubscribeEvent
    public static void ambientTickEvent(ServerTickEvent.Post event) {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            List<BaseAbility> ambientAbilities = ProvenanceDataHandler.getAmbientAbilities(player);
            for (BaseAbility ability : ambientAbilities) {
                if (ability instanceof AmbientAbility ambient && ambient.canExecute(player)) {
                    ambient.tick(player);
                }
            }
        }
    }
}
