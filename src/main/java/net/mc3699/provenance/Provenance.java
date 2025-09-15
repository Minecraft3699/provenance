package net.mc3699.provenance;

import com.mojang.logging.LogUtils;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.registry.ProvArchetypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;
@Mod(Provenance.MODID)
public class Provenance {
    public static final String MODID = "provenance";
    public Provenance(IEventBus modEventBus, ModContainer modContainer) {
        ProvAbilities.register(modEventBus);
        ProvArchetypes.register(modEventBus);
        modEventBus.addListener(this::registerRegistires);
    }

    private void registerRegistires(NewRegistryEvent event)
    {
        event.register(ProvenanceRegistries.ABILITY_REGISTRY);
        event.register(ProvenanceRegistries.ARCHETYPE_REGISTRY);
        event.register(ProvenanceRegistries.WEAKNESS_REGISTRY);
    }

}
