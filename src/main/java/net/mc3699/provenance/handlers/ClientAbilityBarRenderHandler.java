package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.utils.ClientAbilityInfo;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
    private static final ResourceLocation SLOT_COVER = ProvConstants.path("textures/gui/slot_enabled.png");
    // 81x4
    private static final ResourceLocation AP_BAR_EMPTY = ProvConstants.path("textures/gui/ap_bar_empty.png");
    private static final ResourceLocation AP_BAR_FULL = ProvConstants.path("textures/gui/ap_bar_full.png");


    @SubscribeEvent
    public static void onRenderGUI(RenderGuiLayerEvent.Post event)
    {

        Minecraft minecraft = Minecraft.getInstance();
        GuiGraphics graphics = event.getGuiGraphics();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        float apLevel = ProvenanceDataHandler.getAPFromTag(ClientAbilityInfo.clientData);

        // AP Bar
        if(event.getName().equals(VanillaGuiLayers.AIR_LEVEL))
        {
            int apPercent = (int) ((apLevel / ProvenanceDataHandler.MAX_AP) * 81);

            if(apPercent != 81)
            {
                int apX = (screenWidth / 2) + 9;
                int apY = (screenHeight - 44);
                graphics.blit(AP_BAR_EMPTY, apX, apY, 0,0, 81,4,81,4);
                graphics.blit(AP_BAR_FULL, apX, apY, 0,0, apPercent,4,81,4);
            }


        }

        // ability selector thing
        if(event.getName().equals(VanillaGuiLayers.HOTBAR) && ClientAbilityBarHandler.isAbilityBarActive())
        {
            int x = (screenWidth / 2) - 91;
            int y = screenHeight - 75;
            graphics.blit(ABILITY_BAR, x, y, 0, 0, 182, 22, 182, 22);

            // render icons

            if(minecraft.player != null)
            {
                for(int i = 0; i < 8; i++)
                {
                    BaseAbility ability = ProvenanceDataHandler.getAbilityFromTag(ClientAbilityInfo.clientData, i);
                    int slotX = x+3 + i * 20;
                    int slotY = y+3;
                    if(ability != null)
                    {

                        if(ability instanceof ToggleAbility toggleAbility)
                        {
                            renderAbilityIcon(ability.getIcon(), graphics, slotX, slotY, toggleAbility.isEnabled());
                        }



                        // render selected name
                        if(i == ClientAbilityBarHandler.getSelectedSlot())
                        {
                            graphics.drawCenteredString(minecraft.font,ability.getName(),screenWidth/2,y-10,0xFFFFFF);
                        }
                    }


                }
            }

            int slotX = x + (ClientAbilityBarHandler.getSelectedSlot() * 20) -1;
            graphics.blit(ABILITY_SELECTOR, slotX, y-1, 0, 0, 24, 24, 24, 24);
        }
    }


    private static void renderAbilityIcon(ResourceLocation icon, GuiGraphics graphics, int x, int y, boolean enabled)
    {
        graphics.blit(icon, x, y, 0, 0, 16, 16, 16, 16);
        if(enabled)
        {
            graphics.blit(SLOT_COVER, x,y,0,0,16,16,16,16);
        }
    }
}
