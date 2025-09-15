package net.mc3699.provenance.archetype.archetypes;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.List;

public class HumanArchetype extends BaseArchetype {
    @Override
    public Component getName() {
        return Component.literal("Human");
    }

    @Override
    public List<Component> getDescription() {
        return List.of(
                Component.literal("Nothing Special.")
        );
    }

    @Override
    public HashMap<Integer, BaseAbility> getPlayerAbilities() {
        return HashMap.newHashMap(0);
    }

    @Override
    public List<AmbientAbility> getAmbientAbilities() {
        return List.of();
    }
}
