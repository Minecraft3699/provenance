package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.base.BaseAbility;
import net.mc3699.provenance.ability.base.ToggleAbility;
import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = Provenance.MODID)
public class AbilityTickHandler {

    @SubscribeEvent
    public static void tickAbilities(ServerTickEvent.Post event)
    {
        List<ServerPlayer> players = event.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            CompoundTag playerData = player.getPersistentData();
            if(playerData.contains(AbilityDataHandler.ABILITY_TAG))
            {
                CompoundTag abilityData = playerData.getCompound(AbilityDataHandler.ABILITY_TAG);
                for(int i = 0; i < 8; i++) {
                    BaseAbility ability = AbilityDataHandler.getAbilityFromTag(abilityData, i);
                    if (ability instanceof ToggleAbility toggleAbility)
                    {
                        if(toggleAbility.isEnabled() && toggleAbility.isValid(player))
                        {
                            toggleAbility.tick(player);
                        }
                    }
                }
            }
        }
    }

}
