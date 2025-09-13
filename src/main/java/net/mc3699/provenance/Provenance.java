package net.mc3699.provenance;

import com.mojang.logging.LogUtils;
import net.mc3699.provenance.ability.ProvenanceAbilityRegistry;
import net.mc3699.provenance.registry.ProvAbilities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Provenance.MODID)
public class Provenance {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "provenance";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public Provenance(IEventBus modEventBus, ModContainer modContainer) {
        ProvAbilities.register(modEventBus);
        modEventBus.addListener(this::registerRegistires);
    }

    private void registerRegistires(NewRegistryEvent event)
    {
        event.register(ProvenanceAbilityRegistry.ABILITY_REGISTRY);
    }

}
