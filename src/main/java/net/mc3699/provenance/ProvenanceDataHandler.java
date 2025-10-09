package net.mc3699.provenance;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.registry.ProvArchetypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProvenanceDataHandler {

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

    public static void setCooldown(Player player, int slot, int cooldownTicks) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        data.putInt("cooldown_slot_" + slot, cooldownTicks);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static int getCooldown(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        String key = "cooldown_slot_" + slot;
        return data.contains(key) ? data.getInt(key) : 0;
    }

    public static boolean isOnCooldown(Player player, int slot) {
        return getCooldown(player, slot) > 0;
    }

    public static void resetAllCooldowns(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        for (int i = 0; i < 9; i++) {
            data.remove("cooldown_slot_" + i);
        }
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static BaseAbility getAbilityFromTag(CompoundTag data, int i) {
            String key = "slot_" + i;
            if (data.contains(key)) {
                ResourceLocation id = ResourceLocation.parse(data.getString(key));
                return ProvAbilities.ABILITIES.getRegistry().get().get(id);
            }
            return null;
    }

    public static void addAmbientAbility(Player player, ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);

        String idString = abilityId.toString();
        for (Tag t : ambientList) {
            if (t.getAsString().equals(idString)) {
                return;
            }
        }

        ambientList.add(StringTag.valueOf(idString));
        data.put("ambient", ambientList);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static void removeAmbientAbility(Player player, ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);

        String idString = abilityId.toString();
        ListTag newList = new ListTag();

        for (Tag t : ambientList) {
            if (!t.getAsString().equals(idString)) {
                newList.add(t);
            }
        }

        data.put("ambient", newList);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static List<BaseAbility> getAmbientAbilities(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);

        List<BaseAbility> abilities = new ArrayList<>();
        for (Tag t : ambientList) {
            ResourceLocation id = ResourceLocation.parse(t.getAsString());
            BaseAbility ability = ProvAbilities.ABILITIES.getRegistry().get().get(id);
            if (ability != null) {
                abilities.add(ability);
            }
        }
        return abilities;
    }

    public static void applyArchetype(Player player, BaseArchetype archetype) {
        CompoundTag data = new CompoundTag();

        // write slotted abilities
        HashMap<Integer, BaseAbility> slots = archetype.getPlayerAbilities();
        for (var entry : slots.entrySet()) {
            int slot = entry.getKey();
            BaseAbility ability = entry.getValue();
            if (ability != null && ProvAbilities.ABILITIES.getRegistry().get().getKey(ability) != null) {
                ResourceLocation id = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
                data.putString("slot_" + slot, id.toString());
            }
        }

        // write ambient abilities
        ListTag ambientList = new ListTag();
        for (AmbientAbility ability : archetype.getAmbientAbilities()) {
            ResourceLocation id = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
            if (id != null) {
                ambientList.add(StringTag.valueOf(id.toString()));
            }
        }
        data.put("ambient", ambientList);
        data.putFloat("action_points", MAX_AP);
        data.putString("archetype", Objects.requireNonNull(ProvArchetypes.ARCHETYPES.getRegistry().get().getKey(archetype)).toString());

        player.getPersistentData().put(ABILITY_TAG, data);
    }


}
