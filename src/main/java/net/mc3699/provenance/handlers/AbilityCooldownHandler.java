package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber
public class AbilityCooldownHandler {

    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event)
    {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            CompoundTag data = player.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
            boolean changed = false;

            for (int slot = 0; slot < 9; slot++) {
                String key = "cooldown_slot_" + slot;
                if (data.contains(key)) {
                    int cooldown = data.getInt(key);
                    if (cooldown > 0) {
                        cooldown--;
                        if (cooldown <= 0) {
                            data.remove(key);
                        } else {
                            data.putInt(key, cooldown);
                        }
                        changed = true;
                    }
                }
            }

            if (changed) {
                player.getPersistentData().put(ProvenanceDataHandler.ABILITY_TAG, data);
            }
        }

    }

}
