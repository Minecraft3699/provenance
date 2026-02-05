package net.mc3699.provenance.registry;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceRegistries;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.item.ProvItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ProvCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Provenance.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ABILITIES = CREATIVE_TABS.register("abilities", () -> CreativeModeTab.builder().title(Component.literal("Abilities")).icon(ProvCreativeTabs::makeIcon).displayItems(((itemDisplayParameters, output) -> {

        HolderLookup.RegistryLookup<BaseAbility> abilityRegistry = itemDisplayParameters.holders().lookup(ProvenanceRegistries.ABILITY).orElseThrow();

        abilityRegistry.listElements().forEach(holder -> {
            ItemStack stack = new ItemStack(ProvItems.ABILITY_CHIP.get());

            stack.set(ProvComponents.ABILITY_ID.get(), holder.key().location());

            output.accept(stack);
        });

    })).build());


    private static ItemStack makeIcon() {
        ItemStack stack = new ItemStack(ProvItems.ABILITY_CHIP.get());

        ProvAbilities.ABILITIES.getEntries().stream().findFirst().ifPresent(holder -> stack.set(ProvComponents.ABILITY_ID.get(), holder.getId()));

        return stack;
    }

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

}
