package net.mc3699.provenance.ability;

import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ProvenanceAbilityRegistry {

    public static final ResourceKey<Registry<BaseAbility>> ABILITY = ResourceKey.createRegistryKey(ProvConstants.path("ability"));
    public static final Registry<BaseAbility> ABILITY_REGISTRY = new RegistryBuilder<>(ABILITY)
            .sync(true)
            .create();
}
