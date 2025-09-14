package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Provenance.MODID)
public class PlayerPersistenceHandler {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if(!event.isWasDeath()) return;
        CompoundTag oldData = event.getOriginal().getPersistentData().getCompound(AbilityDataHandler.ABILITY_TAG);
        event.getEntity().getPersistentData().put(AbilityDataHandler.ABILITY_TAG, oldData.copy());
    }

}
