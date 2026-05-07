package net.mc3699.provenance.archetype.archetypes;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public class TestArchetype extends BaseArchetype {
    @Override
    public Component getName() {
        return Component.literal("Test Archetype");
    }

    @Override
    public List<Component> getDescription() {
        return List.of(Component.literal("This is a test archetype used for development"));
    }

    @Override
    public Set<ResourceLocation> getGrantedAbilities() {
        return Set.of(
                Provenance.path("cod_rain")
        );
    }

    @Override
    public List<AmbientAbility> getAmbientAbilities() {
        return List.of(ProvAbilities.CARNIVORE_FOOD.get());
    }
}
