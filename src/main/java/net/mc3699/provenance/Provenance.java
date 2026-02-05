package net.mc3699.provenance;

import net.mc3699.provenance.item.ProvItems;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.registry.ProvArchetypes;
import net.mc3699.provenance.registry.ProvComponents;
import net.mc3699.provenance.registry.ProvCreativeTabs;
import net.mc3699.provenance.util.ProvScheduler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.registries.*;

import java.util.function.Supplier;

@Mod(Provenance.MODID)
public class Provenance {
    public static final String MODID = "provenance";
    public Provenance(IEventBus modEventBus, ModContainer modContainer) {
        ProvAbilities.register(modEventBus);
        ProvArchetypes.register(modEventBus);
        modEventBus.addListener(this::registerRegistries);

        ProvComponents.register(modEventBus);
        ProvItems.register(modEventBus);
        ProvCreativeTabs.register(modEventBus);
    }

    private void registerRegistries(NewRegistryEvent event)
    {
        event.register(ProvenanceRegistries.ABILITY_REGISTRY);
        event.register(ProvenanceRegistries.ARCHETYPE_REGISTRY);
    }

}
