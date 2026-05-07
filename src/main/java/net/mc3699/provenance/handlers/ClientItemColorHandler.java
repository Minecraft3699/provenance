package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.item.AbilityChipItem;
import net.mc3699.provenance.item.ProvItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = Provenance.MODID, value = Dist.CLIENT)
public class ClientItemColorHandler {

    @SubscribeEvent
    public static void registerItemColor(RegisterColorHandlersEvent.Item event) {

        event.register((stack, index) -> {

            if(index !=1 ) return 0xFFFFFFFF;

            Level level = Minecraft.getInstance().level;
            if(level == null) return 0xFFFFFFFF;

            BaseAbility ability = AbilityChipItem.getAbility(stack, level);
            if(ability == null) return 0xFFFFFFFF;

            return ability.getColor();
        }, ProvItems.ABILITY_CHIP.get());

    }

}
