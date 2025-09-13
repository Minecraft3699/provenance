package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.AbilityDataHandler;
import net.mc3699.provenance.ability.BaseAbility;
import net.mc3699.provenance.ability.ClientAbilityInfo;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = Provenance.MODID)
public class ClientAbilityBarRenderHandler {

    private static final ResourceLocation ABILITY_BAR = ProvConstants.path("textures/gui/ability_bar.png");
    private static final ResourceLocation ABILITY_SELECTOR = ProvConstants.path("textures/gui/ability_selection.png");


    @SubscribeEvent
    public static void onRenderGUI(RenderGuiLayerEvent.Post event)
    {
        if(event.getName().equals(VanillaGuiLayers.HOTBAR) && ClientAbilityBarHandler.isAbilityBarActive())
        {
            Minecraft minecraft = Minecraft.getInstance();
            GuiGraphics graphics = event.getGuiGraphics();
            int screenWidth = minecraft.getWindow().getGuiScaledWidth();
            int screenHeight = minecraft.getWindow().getGuiScaledHeight();

            int x = (screenWidth / 2) - 91;
            int y = screenHeight - 50;
            graphics.blit(ABILITY_BAR, x, y, 0, 0, 182, 22, 182, 22);

            // render icons

            if(minecraft.player != null)
            {
                for(int i = 0; i < 8; i++)
                {
                    BaseAbility ability = AbilityDataHandler.getAbilityFromTag(ClientAbilityInfo.clientData, i);
                    int slotX = x+3 + i * 20;
                    int slotY = y+3;
                    if(ability != null)
                    {
                        renderAbilityIcon(ability.getIcon(), graphics, slotX, slotY);
                    }
                }
            }

            int slotX = x + (ClientAbilityBarHandler.getSelectedSlot() * 20) -1;
            graphics.blit(ABILITY_SELECTOR, slotX, y, 0, 0, 24, 23, 24, 23);
        }
    }


    private static void renderAbilityIcon(ResourceLocation icon, GuiGraphics graphics, int x, int y)
    {
        graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
    }
}
