package net.mc3699.provenance.abilityMenu;

import net.mc3699.provenance.network.RequestDataSyncPayload;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.mc3699.provenance.util.ProvKeymappings;
import net.mc3699.provenance.abilityMenu.renderUtil.RadialMenuState;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientAbilityBarHandler {

    private static boolean abilityBarActive = false;
    private static boolean prevBarKey = false;
    private static final boolean[] prevAbilityKeys = new boolean[8];

    public static boolean isActive() {
        return abilityBarActive;
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

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        boolean barKeyDown = ProvKeymappings.ABILITY_BAR_KEY.isDown();

        if (barKeyDown && !prevBarKey) {
            Minecraft.getInstance().mouseHandler.releaseMouse();
            PacketDistributor.sendToServer(new RequestDataSyncPayload());
            abilityBarActive = true;
        } else if (!barKeyDown && prevBarKey && abilityBarActive) {
            if (RadialMenuState.hoveredIndex >= 0
                    && RadialMenuState.entries != null
                    && RadialMenuState.hoveredIndex < RadialMenuState.entries.size()) {

                ResourceLocation id =
                        RadialMenuState.entries.get(RadialMenuState.hoveredIndex).id();
                PacketDistributor.sendToServer(new TriggerAbilityPayload(id));
            }
            abilityBarActive = false;
            RadialMenuState.entries = null;
            RadialMenuState.hoveredIndex = -1;
            Minecraft.getInstance().mouseHandler.grabMouse();
        }

        prevBarKey = barKeyDown;

        boolean[] current = {
                ProvKeymappings.ABILITY_1_KEY.isDown(),
                ProvKeymappings.ABILITY_2_KEY.isDown(),
                ProvKeymappings.ABILITY_3_KEY.isDown(),
                ProvKeymappings.ABILITY_4_KEY.isDown(),
                ProvKeymappings.ABILITY_5_KEY.isDown(),
                ProvKeymappings.ABILITY_6_KEY.isDown(),
                ProvKeymappings.ABILITY_7_KEY.isDown(),
                ProvKeymappings.ABILITY_8_KEY.isDown()
        };

        if (RadialMenuState.entries != null) {
            for (int i = 0; i < Math.min(8, RadialMenuState.entries.size()); i++) {
                if (current[i] && !prevAbilityKeys[i]) {
                    ResourceLocation id = RadialMenuState.entries.get(i).id();
                    PacketDistributor.sendToServer(new TriggerAbilityPayload(id));
                }
                prevAbilityKeys[i] = current[i];
            }
        }
    }
}
