package net.mc3699.provenance.archetype.foundation;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseArchetype {

    public abstract Component getName();

    public List<Component> getDescription() {
        return List.of();
    };

    public abstract Set<ResourceLocation> getGrantedAbilities();

    public List<AmbientAbility> getAmbientAbilities() {
        return List.of(

        );
    };
}
