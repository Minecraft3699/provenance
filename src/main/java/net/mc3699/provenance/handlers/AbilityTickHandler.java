package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber
public class AbilityTickHandler {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            tickAmbientAbilities(player);
            tickToggleAbilities(player);
            decrementCooldowns(player);
        }
    }

    private static void tickAmbientAbilities(ServerPlayer player) {
        List<BaseAbility> ambientAbilities = ProvenanceDataHandler.getAmbientAbilities(player);
        for (BaseAbility ability : ambientAbilities) {
            try {
                if (ability.canExecute(player)) {
                    ability.execute(player);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void tickToggleAbilities(ServerPlayer player) {
        for (int slot = 0; slot < 9; slot++) {
            BaseAbility ability = ProvenanceDataHandler.getAbility(player, slot);
            if (!(ability instanceof ToggleAbility toggle)) continue;

            boolean enabled = toggle.isEnabled(player, slot);

            if (enabled) {
                if (toggle.canExecute(player) && toggle.getUseCost() <= ProvenanceDataHandler.getAP(player)) {
                    toggle.tick(player);
                } else {
                    toggle.setEnabled(player, slot, false);
                }
            }
        }
    }

    private static void decrementCooldowns(ServerPlayer player) {
        for (int slot = 0; slot < 9; slot++) {
            int cd = ProvenanceDataHandler.getCooldown(player, slot);
            if (cd > 0) {
                ProvenanceDataHandler.setCooldown(player, slot, cd - 1);
            }
        }
    }
}
