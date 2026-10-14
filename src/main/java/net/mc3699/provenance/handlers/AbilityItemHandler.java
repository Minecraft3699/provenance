package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.abilityItem.IAbilityItem;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Provenance.MODID)
public class AbilityItemHandler {

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event)
    {
        // get player abilities from items
        if(event.getEntity() instanceof LocalPlayer) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();


        BaseArchetype currentArchetype = ProvenanceDataHandler.getArchetype(player);

        List<ItemStack> abilityItems = new ArrayList<>();
        for(ItemStack stack : player.getInventory().items)
        {
            if(stack.getItem() instanceof IAbilityItem)
            {
                abilityItems.add(stack);
            }
        }

        // compile full list of all items in player inventory

        List<ResourceLocation> inventoryAbilities = new ArrayList<>();
        for(ItemStack stack : abilityItems)
        {
            IAbilityItem abilityItem = (IAbilityItem) stack.getItem();
            inventoryAbilities.addAll(abilityItem.inventoryAbilities());
        }

        // add all abilities to player

        for(ResourceLocation ability : inventoryAbilities)
        {
            if(currentArchetype != null)
            {
                if(!currentArchetype.getGrantedAbilities().contains(ability))
                {
                    ProvenanceDataHandler.addAbility(player, ability);
                    ProvenanceDataHandler.setTemporary(player, ability, true);
                }
            } else {
                ProvenanceDataHandler.addAbility(player, ability);
                ProvenanceDataHandler.setTemporary(player, ability, true);
            }
        }

        // remove abilities from items that no longer exist in inventory

        List<BaseAbility> currentAbilities = ProvenanceDataHandler.getAbilities(player);

        for(BaseAbility ability : currentAbilities)
        {
            ResourceLocation abilityID = ProvenanceDataHandler.getIdForAbility(player, ability);
            if(!inventoryAbilities.contains(abilityID))
            {
                if(ProvenanceDataHandler.getTemporary(player, abilityID))
                {
                    ProvenanceDataHandler.setAbilityEnabled(player, abilityID, false);
                    ProvenanceDataHandler.removeAbility(player, abilityID);
                }
            }
        }

    }

}
