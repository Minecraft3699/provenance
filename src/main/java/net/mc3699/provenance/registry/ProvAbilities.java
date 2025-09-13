package net.mc3699.provenance.registry;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.AbilityRegistry;
import net.mc3699.provenance.ability.BaseAbility;
import net.mc3699.provenance.ability.abilities.TestAbility;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ProvAbilities {

    public static final DeferredRegister<BaseAbility> ABILITIES =
            DeferredRegister.create(AbilityRegistry.ABILITY_REGISTRY, Provenance.MODID);


    public static final Supplier<TestAbility> TEST_ABILITY = ABILITIES.register("test_ability", TestAbility::new);





    public static void register(IEventBus eventBus)
    {
        ABILITIES.register(eventBus);
    }
}

