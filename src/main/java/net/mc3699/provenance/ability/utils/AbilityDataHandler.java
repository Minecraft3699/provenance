package net.mc3699.provenance.ability.utils;

import net.mc3699.provenance.ability.base.BaseAbility;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbilityDataHandler {

    public static final String ABILITY_TAG = "provenance_abilities";
    public static final float MAX_AP = 8.0f;

    public static void changeAP(Player player, float amount) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        if (data.contains("action_points"))
        {
            float existing = data.getFloat("action_points");
            float toSet = Mth.clamp(existing + amount, 0,MAX_AP);
            data.putFloat("action_points", toSet);
        } else {
            float toSet = Mth.clamp(MAX_AP + amount, 0,MAX_AP);
            data.putFloat("action_points", toSet);
        }
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static float getAP(ServerPlayer serverPlayer)
    {
        return serverPlayer.getPersistentData().getCompound(ABILITY_TAG).getFloat("action_points");
    }

    public static float getAPFromTag(CompoundTag tag)
    {
        return tag.getFloat("action_points");
    }

    public static void setAbility(Player player, int slot, @Nullable ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        String key = "slot_" + slot;

        if (abilityId != null) {
            data.putString(key, abilityId.toString());
        } else {
            data.remove(key);
        }

        player.getPersistentData().put(ABILITY_TAG, data);
    }


    public static ResourceLocation getAbilityId(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        String key = "slot_" + slot;
        return data.contains(key) ? ResourceLocation.parse(data.getString(key)) : null;
    }

    public static BaseAbility getAbility(Player player, int slot) {
        ResourceLocation id = getAbilityId(player, slot);
        return id != null ? ProvAbilities.ABILITIES.getRegistry().get().get(id) : null;
    }

    public static List<BaseAbility> getAbilities(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        List<BaseAbility> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String key = "slot_" + i;
            if (data.contains(key)) {
                ResourceLocation id = ResourceLocation.parse(data.getString(key));
                BaseAbility ability = ProvAbilities.ABILITIES.getRegistry().get().get(id);
                if (ability != null) {
                    slots.add(ability);
                }
            }
        }
        return slots;
    }

    public static BaseAbility getAbilityFromTag(CompoundTag data, int i) {
            String key = "slot_" + i;
            if (data.contains(key)) {
                ResourceLocation id = ResourceLocation.parse(data.getString(key));
                return ProvAbilities.ABILITIES.getRegistry().get().get(id);
            }
            return null;
    }

}
