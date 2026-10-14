package net.mc3699.provenance.item;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.abilities.TestAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ProvItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(Provenance.MODID);

    public static final Supplier<AbilityChipItem> ABILITY_CHIP = ITEMS.register("ability_chip", () ->
            new AbilityChipItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    public static final Supplier<TestAbilityItem> ABILITY_TEST_ITEM = ITEMS.register("ability_test_item", () ->
            new TestAbilityItem(new Item.Properties().stacksTo(1)));
;
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
