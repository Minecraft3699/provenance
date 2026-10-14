package net.mc3699.provenance.ability.foundation.abilityItem;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.utils.AbilityExecutor;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public interface IAbilityItem {
    List<ResourceLocation> inventoryAbilities();

    default void tick() {

    }
}