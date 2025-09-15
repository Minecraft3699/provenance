package net.mc3699.provenance.registry;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.abilities.*;
import net.mc3699.provenance.ProvenanceRegistries;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ProvAbilities {

    public static final DeferredRegister<BaseAbility> ABILITIES =
            DeferredRegister.create(ProvenanceRegistries.ABILITY_REGISTRY, Provenance.MODID);

    public static final Supplier<CodRainAbility> COD_RAIN =
            ABILITIES.register("cod_rain", CodRainAbility::new);

    public static final Supplier<BurgerAbility> BURGER =
            ABILITIES.register("burger", BurgerAbility::new);

    public static final Supplier<AmbientNightVisionAbility> NIGHT_VISION_AMBIENT =
            ABILITIES.register("night_vision_ambient", AmbientNightVisionAbility::new);

    public static final Supplier<RageAbility> RAGE =
            ABILITIES.register("rage", RageAbility::new);

    public static final Supplier<AmbientEatEntityAbility> EAT_ENTITY =
            ABILITIES.register("eat_entity", AmbientEatEntityAbility::new);


    public static void register(IEventBus eventBus)
    {
        ABILITIES.register(eventBus);
    }
}

