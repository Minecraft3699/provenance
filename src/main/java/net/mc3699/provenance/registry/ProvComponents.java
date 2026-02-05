package net.mc3699.provenance.registry;

import net.mc3699.provenance.Provenance;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ProvComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Provenance.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResourceLocation>> ABILITY_ID =
            COMPONENTS.register("ability",
                    () ->
                    DataComponentType.<ResourceLocation>builder()
                            .persistent(ResourceLocation.CODEC)
                            .networkSynchronized(ResourceLocation.STREAM_CODEC)
                            .build()
            );

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}
