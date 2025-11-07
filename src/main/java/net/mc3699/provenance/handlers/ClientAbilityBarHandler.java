package net.mc3699.provenance.handlers;

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
        if (ProvKeymappings.ABILITY_BAR_KEY.isDown()) {
            PacketDistributor.sendToServer(new RequestDataSyncPayload());
            abilityBarActive = true;
        } else {
            if (abilityBarActive) {
                if (selectedSlot != 0) {
                    PacketDistributor.sendToServer(new TriggerAbilityPayload(selectedSlot, true));
                }
                selectedSlot = 0;
            }
            abilityBarActive = false;
        }

        if (ProvKeymappings.ABILITY_1_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(1, true));
        }

        if (ProvKeymappings.ABILITY_2_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(2, true));
        }

        if (ProvKeymappings.ABILITY_3_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(3, true));
        }

        if (ProvKeymappings.ABILITY_4_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(4, true));
        }

        if (ProvKeymappings.ABILITY_5_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(5, true));
        }

        if (ProvKeymappings.ABILITY_6_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(6, true));
        }

        if (ProvKeymappings.ABILITY_7_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(7, true));
        }

        if (ProvKeymappings.ABILITY_8_KEY.isDown()) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(8, true));
        }
    }

    @SubscribeEvent
    public static void onScroll(InputEvent.MouseScrollingEvent event) {
        if (abilityBarActive) {
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
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ProvKeymappings.ABILITY_BAR_KEY);
        event.register(ProvKeymappings.ABILITY_1_KEY);
        event.register(ProvKeymappings.ABILITY_2_KEY);
        event.register(ProvKeymappings.ABILITY_3_KEY);
        event.register(ProvKeymappings.ABILITY_4_KEY);
        event.register(ProvKeymappings.ABILITY_5_KEY);
        event.register(ProvKeymappings.ABILITY_6_KEY);
        event.register(ProvKeymappings.ABILITY_7_KEY);
        event.register(ProvKeymappings.ABILITY_8_KEY);
    }

}