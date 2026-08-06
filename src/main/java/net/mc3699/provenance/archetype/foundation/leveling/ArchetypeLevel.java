package net.mc3699.provenance.archetype.foundation.leveling;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;

public record ArchetypeLevel(int level,
                             int xpRequirement,
                             Set<ResourceLocation> abilities,
                             List<AmbientAbility> ambientAbilities
) {

}