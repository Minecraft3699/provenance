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

    private static final boolean[] prevAbilityKeys = new boolean[8];
    private static boolean abilityBarActive;
    private static int selectedSlot = 0;
    private static boolean prevBarKey = false;

    @SubscribeEvent
    public static void triggerAbility(ClientTickEvent.Post event) {
        boolean barKeyDown = ProvKeymappings.ABILITY_BAR_KEY.isDown();

        if (barKeyDown && !prevBarKey) {
            PacketDistributor.sendToServer(new RequestDataSyncPayload());
            abilityBarActive = true;
        } else if (!barKeyDown && prevBarKey) {
            if (selectedSlot != 0) {
                PacketDistributor.sendToServer(new TriggerAbilityPayload(selectedSlot, true));
            }
            selectedSlot = 0;
            abilityBarActive = false;
        }

        prevBarKey = barKeyDown;

        boolean[] current = {ProvKeymappings.ABILITY_1_KEY.isDown(), ProvKeymappings.ABILITY_2_KEY.isDown(), ProvKeymappings.ABILITY_3_KEY.isDown(), ProvKeymappings.ABILITY_4_KEY.isDown(), ProvKeymappings.ABILITY_5_KEY.isDown(), ProvKeymappings.ABILITY_6_KEY.isDown(), ProvKeymappings.ABILITY_7_KEY.isDown(), ProvKeymappings.ABILITY_8_KEY.isDown()};

        for (int i = 0; i < 8; i++) {
            if (current[i] && !prevAbilityKeys[i]) {
                PacketDistributor.sendToServer(new TriggerAbilityPayload(i + 1, true));
            }
            prevAbilityKeys[i] = current[i];
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