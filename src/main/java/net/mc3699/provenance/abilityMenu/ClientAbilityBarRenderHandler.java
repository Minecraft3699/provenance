package net.mc3699.provenance.abilityMenu;

import com.mojang.blaze3d.platform.InputConstants;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.ability.utils.ClientAbilityInfo;
import net.mc3699.provenance.abilityMenu.keybind.AbilityKeybind;
import net.mc3699.provenance.abilityMenu.keybind.AbilityKeybindHandler;
import net.mc3699.provenance.abilityMenu.renderUtil.RadialMenuState;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientAbilityBarRenderHandler {

    private static final ResourceLocation AP_BAR_EMPTY = ProvConstants.path("textures/gui/ap_bar_empty.png");
    private static final ResourceLocation AP_BAR_FULL = ProvConstants.path("textures/gui/ap_bar_full.png");
    private static float animationTimer = 0f;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != InputConstants.PRESS) return;

        if (AbilityKeybindHandler.isAssigning()) {
            if (event.getKey() != InputConstants.KEY_ESCAPE) {
                AbilityKeybindHandler.setBinding(AbilityKeybindHandler.getAssigningAbility(), new AbilityKeybind(event.getKey(), event.getModifiers()));
            }
            AbilityKeybindHandler.finishAssign();
            return;
        }

        if (Minecraft.getInstance().screen == null) {
            for (ResourceLocation abilityId : AbilityKeybindHandler.getAllBoundAbilities()) {
                if (AbilityKeybindHandler.matches(abilityId, event.getKey(), event.getModifiers())) {
                    PacketDistributor.sendToServer(new TriggerAbilityPayload(abilityId));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (event.getAction() != InputConstants.PRESS) return;
        if (!ClientAbilityBarHandler.isActive() || animationTimer < 1.0f) return;

        int hovered = RadialMenuState.hoveredIndex;
        if (hovered < 0 || hovered >= RadialMenuState.entries.size()) return;

        AbilityEntry entry = RadialMenuState.entries.get(hovered);

        if (AbilityKeybindHandler.isAssigning()) {
            if (event.getButton() == 1) {
                AbilityKeybindHandler.clearBinding(AbilityKeybindHandler.getAssigningAbility());
            } else {
                AbilityKeybindHandler.setBinding(AbilityKeybindHandler.getAssigningAbility(), new AbilityKeybind(InputConstants.Type.MOUSE.getOrCreate(event.getButton()).getValue(), 0));
            }
            AbilityKeybindHandler.finishAssign();
            event.setCanceled(true);
            return;
        }

        if (event.getButton() == InputConstants.MOUSE_BUTTON_MIDDLE) {
            AbilityKeybindHandler.beginAssign(entry.id());
            event.setCanceled(true);
        } else if (event.getButton() == InputConstants.MOUSE_BUTTON_LEFT) {
            PacketDistributor.sendToServer(new TriggerAbilityPayload(entry.id()));
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        CompoundTag data = ClientAbilityInfo.clientData;

        if (event.getName().equals(VanillaGuiLayers.AIR_LEVEL)) {
            renderAPBar(graphics, data, screenWidth, screenHeight);
        }

        if (!event.getName().equals(VanillaGuiLayers.HOTBAR)) return;

        boolean active = ClientAbilityBarHandler.isActive();
        float delta = mc.getTimer().getGameTimeDeltaTicks() * 0.15f;

        if (active) {
            animationTimer = Math.min(1.0f, animationTimer + delta);
        } else {
            animationTimer = Math.max(0.0f, animationTimer - delta);
        }

        if (animationTimer <= 0f) {
            RadialMenuState.hoveredIndex = -1;
            return;
        }

        List<AbilityEntry> entries = fetchAbilities(data);
        if (entries.isEmpty()) return;

        float easeProgress = animationTimer * animationTimer * (3.0f - 2.0f * animationTimer);
        int cx = screenWidth / 2;
        int cy = screenHeight / 2;
        int targetRadius = 48;
        int currentRadius = (int) (targetRadius * easeProgress);

        int hovered = getHoveredIndex(mc, cx, cy, currentRadius, entries.size());

        if (AbilityKeybindHandler.isAssigning()) {
            graphics.drawCenteredString(mc.font, "Press key to bind (Right-Click to clear bindings)", cx, cy - 60, 0xFFFF00);
        }

        for (int i = 0; i < entries.size(); i++) {
            AbilityEntry entry = entries.get(i);
            BaseAbility ability = entry.ability();

            double angle = (i / (double) entries.size()) * Math.PI * 2 - Math.PI / 2;
            int x = cx + (int) (Math.cos(angle) * currentRadius);
            int y = cy + (int) (Math.sin(angle) * currentRadius);

            boolean isHovered = i == hovered && animationTimer >= 1.0f;
            float scale = (isHovered ? 1.25f : 1.0f) * easeProgress;
            float alpha = (isHovered ? 1.0f : 0.45f) * animationTimer;

            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            graphics.pose().scale(scale, scale, 1);
            graphics.pose().translate(-8, -8, 0);

            graphics.setColor(1f, 1f, 1f, alpha);
            renderAbilityIcon(graphics, ability.getIcon(), 0, 0, entry.enabled());

            int cooldown = data.getInt("cooldown_" + entry.id());
            if (cooldown > 0) {
                float progress = Math.min(1f, (float) cooldown / Math.max(1f, ability.getCooldown()));
                renderCooldownOverlay(graphics, 0, 0, progress);
            }

            AbilityKeybind bind = AbilityKeybindHandler.getBinding(entry.id());
            if (bind != null && animationTimer >= 0.8f) {
                String keyName = InputConstants.getKey(bind.keyCode(), -1).getDisplayName().getString();
                graphics.pose().pushPose();
                graphics.pose().scale(0.5f, 0.5f, 1f);
                graphics.drawCenteredString(mc.font, keyName, 16, 32, 0xFFFFFF);
                graphics.pose().popPose();
            }

            graphics.setColor(1f, 1f, 1f, 1f);
            graphics.pose().popPose();
        }

        if (hovered != -1 && !AbilityKeybindHandler.isAssigning() && animationTimer >= 1.0f) {
            graphics.drawCenteredString(mc.font, entries.get(hovered).ability().getName().getString(), cx, cy + targetRadius + 10, 0xFFFFFF);
        }

        RadialMenuState.hoveredIndex = (animationTimer >= 1.0f) ? hovered : -1;
        RadialMenuState.entries = entries;
    }

    private static void renderAPBar(GuiGraphics graphics, CompoundTag data, int screenWidth, int screenHeight) {
        float ap = data.contains("action_points") ? data.getFloat("action_points") : ProvenanceDataHandler.MAX_AP;
        if (ap < ProvenanceDataHandler.MAX_AP) {
            int apWidth = (int) ((ap / ProvenanceDataHandler.MAX_AP) * 81);
            int apX = (screenWidth / 2) + 9;
            int apY = screenHeight - 44;
            graphics.blit(AP_BAR_EMPTY, apX, apY, 0, 0, 81, 4, 81, 4);
            if (apWidth > 0) graphics.blit(AP_BAR_FULL, apX, apY, 0, 0, apWidth, 4, 81, 4);
        }
    }

    private static List<AbilityEntry> fetchAbilities(CompoundTag data) {
        List<AbilityEntry> entries = new ArrayList<>();
        ListTag list = data.getList("abilities", Tag.TAG_STRING);
        for (Tag t : list) {
            ResourceLocation id = ResourceLocation.tryParse(t.getAsString());
            if (id == null) continue;
            BaseAbility ability = ProvAbilities.ABILITIES.getRegistry().get().get(id);
            if (ability == null) continue;
            boolean enabled = !(ability instanceof ToggleAbility) || data.getBoolean("enabled_" + id);
            entries.add(new AbilityEntry(id, enabled, ability));
        }
        return entries;
    }

    private static int getHoveredIndex(Minecraft mc, int cx, int cy, int radius, int count) {
        double mx = mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth();
        double my = mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight();
        int hovered = -1;
        double best = Double.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            double angle = (i / (double) count) * Math.PI * 2 - Math.PI / 2;
            double x = cx + Math.cos(angle) * radius;
            double y = cy + Math.sin(angle) * radius;
            double dx = mx - x;
            double dy = my - y;
            double d = dx * dx + dy * dy;
            if (d < best && d < 1200) {
                best = d;
                hovered = i;
            }
        }
        return hovered;
    }

    private static void renderAbilityIcon(GuiGraphics graphics, ResourceLocation icon, int x, int y, boolean enabled) {
        graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
        if (!enabled) {
            graphics.setColor(0.5f, 0.5f, 0.5f, 1f);
            graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
            graphics.setColor(1f, 1f, 1f, 1f);
        }
    }

    private static void renderCooldownOverlay(GuiGraphics graphics, int x, int y, float progress) {
        if (progress <= 0f) return;
        int height = Math.round(16 * progress);
        int offsetY = 16 - height;
        graphics.fill(x, y + offsetY, x + 16, y + 16, 0x80FFFFFF);
    }

    public record AbilityEntry(ResourceLocation id, boolean enabled, BaseAbility ability) {
    }
}