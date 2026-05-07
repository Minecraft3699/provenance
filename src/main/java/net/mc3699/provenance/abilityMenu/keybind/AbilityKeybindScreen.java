package net.mc3699.provenance.abilityMenu.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbilityKeybindScreen extends Screen {

    private final ResourceLocation abilityId;

    public AbilityKeybindScreen(ResourceLocation abilityId) {
        super(Component.literal("Assign Key"));
        this.abilityId = abilityId;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputConstants.KEY_ESCAPE) {
            this.minecraft.setScreen(null);
            return true;
        }

        AbilityKeybindHandler.setBinding(
                abilityId,
                new AbilityKeybind(keyCode, modifiers)
        );

        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == 1) {
            AbilityKeybindHandler.clearBinding(abilityId);
            this.minecraft.setScreen(null);
            return true;
        }

        int mouseButtonCode = InputConstants.Type.MOUSE.getOrCreate(button).getValue();

        AbilityKeybindHandler.setBinding(
                abilityId,
                new AbilityKeybind(mouseButtonCode, 0)
        );

        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g, mouseX, mouseY, partialTick);

        int cx = this.width / 2;
        int cy = this.height / 2;

        g.drawCenteredString(this.font, "Binding Ability...", cx, cy - 20, 0xFFCC00);
        g.drawCenteredString(this.font, "Press any Key or Mouse Button", cx, cy, 0xFFFFFF);
        g.drawCenteredString(this.font, "Right-Click to Clear Binding", cx, cy + 20, 0xAAAAAA);
        g.drawCenteredString(this.font, "ESC to Cancel", cx, cy + 40, 0xAAAAAA);

        super.render(g, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}