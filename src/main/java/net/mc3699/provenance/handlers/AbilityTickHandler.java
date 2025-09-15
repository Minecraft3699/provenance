package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Provenance.MODID)
public class AbilityTickHandler {

    @SubscribeEvent
    public static void tickAbilities(ServerTickEvent.Pre event)
    {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            CompoundTag playerData = player.getPersistentData();
            if(playerData.contains(ProvenanceDataHandler.ABILITY_TAG))
            {
                CompoundTag abilityData = playerData.getCompound(ProvenanceDataHandler.ABILITY_TAG);
                for(int i = 0; i < 8; i++) {
                    BaseAbility ability = ProvenanceDataHandler.getAbilityFromTag(abilityData, i);
                    if (ability instanceof ToggleAbility toggleAbility)
                    {
                        if(toggleAbility.isEnabled() && toggleAbility.canExecute(player))
                        {
                            toggleAbility.tick(player);
                        } else if(!toggleAbility.canExecute(player))
                        {
                            toggleAbility.setEnabled(false);
                        }
                    }
                }
            }
        }
    }

}
