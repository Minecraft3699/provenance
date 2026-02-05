package net.mc3699.provenance.item;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ProvItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Provenance.MODID);

    public static final Supplier<AbilityChipItem> ABILITY_CHIP = ITEMS.register("ability_chip", () ->
            new AbilityChipItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
