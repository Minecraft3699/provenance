package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.network.RequestDataSyncPayload;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.mc3699.provenance.util.ProvKeymappings;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientAbilityBarHandler {

    private static boolean abilityBarActive;
    private static int selectedSlot = 0;

    @SubscribeEvent
    public static void triggerAbility(ClientTickEvent.Post event) {
        if(ProvKeymappings.ABILITY_KEY.isDown())
        {
            PacketDistributor.sendToServer(new RequestDataSyncPayload());
            abilityBarActive = true;
        } else {
            if(abilityBarActive)
            {
                if(selectedSlot != 0)
                {
                    PacketDistributor.sendToServer(new TriggerAbilityPayload(selectedSlot, true));
                }
                selectedSlot = 0;
            }
            abilityBarActive = false;
        }
    }

    @SubscribeEvent
    public static void onScroll(InputEvent.MouseScrollingEvent event)
    {
        if(abilityBarActive)
        {
            double scroll = event.getScrollDeltaY();
            if (scroll > 0) selectedSlot = (selectedSlot + 8) % 9;
            else if (scroll < 0) selectedSlot = (selectedSlot + 1) % 9;
            event.setCanceled(true);
        }
    }

    public static boolean isAbilityBarActive() {
        return abilityBarActive;
    }

    public static int getSelectedSlot() {
        return selectedSlot;
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(ProvKeymappings.ABILITY_KEY);
    }

}
