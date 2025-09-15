package net.mc3699.provenance.archetype.archetypes;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;

public class ExampleArchetype extends BaseArchetype {
    @Override
    public Component getName() {
        return Component.literal("Test Archetype");
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                Component.literal("This is an example of how to make an archetype"),
                Component.literal("This is how you add more lines to a description"),
                Component.literal("One line per component object")
        );
    }

    @Override
    public HashMap<Integer, BaseAbility> getPlayerAbilities()
    {
        HashMap<Integer, BaseAbility> abilities = new HashMap<>();
        abilities.put(1, ProvAbilities.RAGE.get());
        return abilities;
    }

    @Override
    public List<AmbientAbility> getAmbientAbilities() {
        return List.of(
                ProvAbilities.NIGHT_VISION_AMBIENT.get(),
                ProvAbilities.EAT_ENTITY.get()
        );
    }
}
