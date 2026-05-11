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
    }
}
