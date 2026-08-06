package net.mc3699.provenance.archetype.archetypes;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.archetype.foundation.leveling.ArchetypeLevel;
import net.mc3699.provenance.archetype.foundation.leveling.LevelingArchetype;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Set;

public class LevelingTestArchetype extends LevelingArchetype {
    @Override
    public List<ArchetypeLevel> getLevels() {
        return List.of(
                // Base Level
                new ArchetypeLevel(
                        0,
                        0,
                        Set.of(Provenance.path("test")),
                        List.of()),
                // more cod
                new ArchetypeLevel(
                        1,
                        100,
                        Set.of(Provenance.path("cod_rain"), Provenance.path("test_ability")),
                        List.of(ProvAbilities.CARNIVORE_FOOD.get())
                )
        );
    }

    @Override
    public Component getName() {
        return Component.literal("Leveling Test");
    }
}
