package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.ability.utils.ClientAbilityInfo;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientAbilityBarRenderHandler {

    private static final ResourceLocation ABILITY_BAR = ProvConstants.path("textures/gui/ability_bar.png");
    private static final ResourceLocation ABILITY_SELECTOR = ProvConstants.path("textures/gui/ability_selection.png");
    private static final ResourceLocation AP_BAR_EMPTY = ProvConstants.path("textures/gui/ap_bar_empty.png");
    private static final ResourceLocation AP_BAR_FULL = ProvConstants.path("textures/gui/ap_bar_full.png");
    private static final ResourceLocation SLOT_COVER = ProvConstants.path("textures/gui/slot_enabled.png");

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        CompoundTag data = ClientAbilityInfo.clientData;

        if (event.getName().equals(VanillaGuiLayers.AIR_LEVEL)) {
            float ap = data.contains("action_points") ? data.getFloat("action_points") : ProvenanceDataHandler.MAX_AP;
            if (ap < ProvenanceDataHandler.MAX_AP) {
                int apWidth = (int) ((ap / ProvenanceDataHandler.MAX_AP) * 81);
                int apX = (screenWidth / 2) + 9;
                int apY = screenHeight - 44;
                graphics.blit(AP_BAR_EMPTY, apX, apY, 0, 0, 81, 4, 81, 4);
                if (apWidth > 0) graphics.blit(AP_BAR_FULL, apX, apY, 0, 0, apWidth, 4, 81, 4);
            }
        }

        if (event.getName().equals(VanillaGuiLayers.HOTBAR) && ClientAbilityBarHandler.isAbilityBarActive()) {
            int x = (screenWidth / 2) - 91;
            int y = screenHeight - 75;
            graphics.setColor(1f, 1f, 1f, 0.85f);
            graphics.blit(ABILITY_BAR, x, y, 0, 0, 182, 22, 182, 22);
            graphics.setColor(1f, 1f, 1f, 1f);

            for (int i = 0; i < 9; i++) {
                String slotKey = "slot_" + i;
                if (!data.contains(slotKey)) continue;
                String rawId = data.getString(slotKey);
                if (rawId.isEmpty()) continue;
                ResourceLocation abilityId = ResourceLocation.tryParse(rawId);
                if (abilityId == null) continue;
                BaseAbility ability = ProvAbilities.ABILITIES.getRegistry().get().get(abilityId);
                if (ability == null) continue;

                int slotX = x + 3 + i * 20;
                int slotY = y + 3;
                boolean enabled = data.getBoolean("slot_enabled_" + i);
                renderAbilityIcon(graphics, ability.getIcon(), slotX, slotY, enabled);

                int cooldown = data.contains("cooldown_slot_" + i) ? data.getInt("cooldown_slot_" + i) : 0;
                if (cooldown > 0) {
                    float progress = Math.min(1f, (float) cooldown / Math.max(1f, ability.getCooldown()));
                    renderCooldownOverlay(graphics, slotX, slotY, progress);
                }

                if (i == ClientAbilityBarHandler.getSelectedSlot())
                    graphics.drawCenteredString(mc.font, ability.getName(), screenWidth / 2, y - 10, 0xFFFFFF);
            }

            int selectedX = x + (ClientAbilityBarHandler.getSelectedSlot() * 20) - 1;
            graphics.blit(ABILITY_SELECTOR, selectedX, y - 1, 0, 0, 24, 24, 24, 24);
        }
    }

    private static void renderAbilityIcon(GuiGraphics graphics, ResourceLocation icon, int x, int y, boolean enabled) {
        graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
        if (!enabled) {
            graphics.setColor(0.5f, 0.5f, 0.5f, 1f);
            graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
            graphics.setColor(1f, 1f, 1f, 1f);
        } else {
            graphics.blit(SLOT_COVER, x, y, 0, 0, 16, 16, 16, 16);
        }
    }

    private static void renderCooldownOverlay(GuiGraphics graphics, int x, int y, float progress) {
        if (progress <= 0f) return;
        int height = Math.round(16 * progress);
        int offsetY = 16 - height;
        int color = 0x80FFFFFF;
        graphics.fill(x, y + offsetY, x + 16, y + 16, color);
    }
}
