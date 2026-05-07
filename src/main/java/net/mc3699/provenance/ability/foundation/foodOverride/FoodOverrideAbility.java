package net.mc3699.provenance.ability.foundation.foodOverride;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import java.util.Map;

public abstract class FoodOverrideAbility extends AmbientAbility {

    public abstract Map<Item,FoodOverrideData> getFoodOverrides();

    @Override
    public void tick(ServerPlayer player) {
        // Nothing
    }
}
