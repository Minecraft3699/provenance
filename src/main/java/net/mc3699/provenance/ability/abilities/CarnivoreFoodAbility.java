package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.foodOverride.FoodOverrideAbility;
import net.mc3699.provenance.ability.foundation.foodOverride.FoodOverrideData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarnivoreFoodAbility extends FoodOverrideAbility {
    @Override
    public Map<Item, FoodOverrideData> getFoodOverrides() {
        Map<Item, FoodOverrideData> overrides = new HashMap<>();

        overrides.put(Items.BREAD, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.APPLE, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.BEETROOT, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.MELON, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.CARROT, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.COOKIE, new FoodOverrideData(0,0,
                List.of(new MobEffectInstance(MobEffects.POISON, 120, 3, false, true))));
        overrides.put(Items.SWEET_BERRIES, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.POTATO, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.BAKED_POTATO, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.POISONOUS_POTATO, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.PUMPKIN_PIE, new FoodOverrideData(0,0, List.of()));
        overrides.put(Items.BONE, new FoodOverrideData(4, 2, List.of()));

        return overrides;
    }

    @Override
    public Component getName() {
        return Component.literal("Carnivorous");
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }

    @Override
    public int getColor() {
        return 0xFF0000;
    }
}
