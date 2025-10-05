package net.mc3699.provenance.registry;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceRegistries;
import net.mc3699.provenance.archetype.archetypes.HumanArchetype;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ProvArchetypes {

    public static final DeferredRegister<BaseArchetype> ARCHETYPES =
            DeferredRegister.create(ProvenanceRegistries.ARCHETYPE_REGISTRY, Provenance.MODID);

    public static final Supplier<BaseArchetype> HUMAN = ARCHETYPES.register("human", HumanArchetype::new);

    public static void register(IEventBus eventBus)
    {
        ARCHETYPES.register(eventBus);
    }

}
