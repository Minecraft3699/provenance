package net.mc3699.provenance.archetype.foundation;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.List;

public abstract class BaseArchetype {

    public abstract Component getName();

    public abstract List<Component> getDescription();

    public abstract HashMap<Integer,BaseAbility> getPlayerAbilities();

    public abstract List<AmbientAbility> getAmbientAbilities();

}
