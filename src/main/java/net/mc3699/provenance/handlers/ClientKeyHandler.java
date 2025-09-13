package net.mc3699.provenance.handlers;

import net.mc3699.provenance.util.ProvKeymappings;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientKeyHandler {

    @SubscribeEvent
    public static void triggerAbility(ClientTickEvent.Post event) {
        while (ProvKeymappings.ABILITY_KEY.consumeClick()) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("TESTING!!"));
        }
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(ProvKeymappings.ABILITY_KEY);
    }

}
