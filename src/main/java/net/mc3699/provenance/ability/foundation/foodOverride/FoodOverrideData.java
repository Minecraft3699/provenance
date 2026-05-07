package net.mc3699.provenance.ability.foundation.foodOverride;

import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public record FoodOverrideData(float nutrition, float saturation, List<MobEffectInstance> effects) {
}
