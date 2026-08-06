package net.mc3699.provenance.archetype.foundation.leveling;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class LevelingArchetype extends BaseArchetype {

    public abstract List<ArchetypeLevel> getLevels();

    public ArchetypeLevel getArchetypeLevel(int xp)
    {
        ArchetypeLevel currentLevel = null;
        for(ArchetypeLevel level : getLevels())
        {
            if(xp >= level.xpRequirement()) {
                currentLevel = level;
            } else {
                break;
            }
        }
        return currentLevel;
    }

    public Set<ResourceLocation> getActiveAbilitiesForXP(int xp) {
        return getArchetypeLevel(xp).abilities();
    }

    public List<AmbientAbility> getAmbientAbilitiesForXP(int xp)
    {
        return getArchetypeLevel(xp).ambientAbilities();
    }


    @Override
    public Set<ResourceLocation> getGrantedAbilities() {
        return Set.of();
    }
}
