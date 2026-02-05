package net.mc3699.provenance.abilityMenu.keybind;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbilityKeybindScreen extends Screen {

    private final ResourceLocation abilityId;
    private boolean waitingForInput = true;

    public AbilityKeybindScreen(ResourceLocation abilityId) {
        super(Component.literal("Assign Key"));
        this.abilityId = abilityId;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!waitingForInput) return super.keyPressed(keyCode, scanCode, modifiers);

        AbilityKeybindHandler.setBinding(
                abilityId,
                new AbilityKeybind(keyCode, modifiers)
        );

        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (!waitingForInput) return super.mouseClicked(x, y, button);

        AbilityKeybindHandler.setBinding(
                abilityId,
                new AbilityKeybind(button, 0)
        );

        this.minecraft.setScreen(null);
        return true;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.drawCenteredString(
                this.font,
                "Press a key to bind this ability",
                this.width / 2,
                this.height / 2,
                0xFFFFFF
        );
        super.render(g, mouseX, mouseY, partialTick);
    }
}