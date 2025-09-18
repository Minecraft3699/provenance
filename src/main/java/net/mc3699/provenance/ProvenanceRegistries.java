package net.mc3699.provenance;

import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ProvenanceRegistries {

    public static final ResourceKey<Registry<BaseAbility>> ABILITY = ResourceKey.createRegistryKey(ProvConstants.path("ability"));
    public static final ResourceKey<Registry<BaseArchetype>> ARCHETYPE = ResourceKey.createRegistryKey(ProvConstants.path("archetype"));
    public static final Registry<BaseAbility> ABILITY_REGISTRY = new RegistryBuilder<>(ABILITY)
            .sync(true)
            .create();

    public static final Registry<BaseArchetype> ARCHETYPE_REGISTRY = new RegistryBuilder<>(ARCHETYPE)
            .sync(true)
            .create();

}
