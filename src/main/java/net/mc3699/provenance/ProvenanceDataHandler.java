package net.mc3699.provenance;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ProvenanceDataHandler {

    public static final String ABILITY_TAG = "provenance_abilities";
    public static final float MAX_AP = 8.0f;

    private static final Map<UUID, Map<Integer, BaseAbility>> playerAbilities = new HashMap<>();

    public static void changeAP(Player player, float amount) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        float existing = data.contains("action_points") ? data.getFloat("action_points") : MAX_AP;
        data.putFloat("action_points", Mth.clamp(existing + amount, 0, MAX_AP));
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static BaseAbility getAbilityFromClientTag(CompoundTag data, int slot) {
        String key = "slot_" + slot;
        if (!data.contains(key)) return null;
        String raw = data.getString(key);
        if (raw.isEmpty()) return null;

        try {
            ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id == null) return null;
            BaseAbility template = ProvAbilities.ABILITIES.getRegistry().get().get(id);
            if (template == null) return null;
            return template.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static float getAPFromClientTag(CompoundTag data) {
        if (data == null) return MAX_AP;
        return data.contains("action_points") ? data.getFloat("action_points") : MAX_AP;
    }

    public static float getAP(ServerPlayer serverPlayer) {
        return serverPlayer.getPersistentData().getCompound(ABILITY_TAG).copy().getFloat("action_points");
    }

    public static void setAbility(Player player, int slot, @Nullable ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        String key = "slot_" + slot;
        if (abilityId != null) {
            data.putString(key, abilityId.toString());
        } else {
            data.remove(key);
        }
        player.getPersistentData().put(ABILITY_TAG, data);
        if (playerAbilities.containsKey(player.getUUID())) {
            playerAbilities.get(player.getUUID()).remove(slot);
        }
    }

    public static @Nullable ResourceLocation getAbilityId(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        String key = "slot_" + slot;
        if (!data.contains(key)) return null;
        String raw = data.getString(key);
        if (raw.isEmpty()) return null;
        try {
            return ResourceLocation.parse(raw);
        } catch (Exception e) {
            return null;
        }
    }

    public static @Nullable BaseAbility getAbility(Player player, int slot) {
        UUID playerId = player.getUUID();
        Map<Integer, BaseAbility> abilities = playerAbilities.computeIfAbsent(playerId, k -> new HashMap<>());
        if (abilities.containsKey(slot)) return abilities.get(slot);
        ResourceLocation id = getAbilityId(player, slot);
        if (id == null) return null;
        BaseAbility templateAbility = ProvAbilities.ABILITIES.getRegistry().get().get(id);
        if (templateAbility == null) return null;
        try {
            BaseAbility instance = templateAbility.getClass().getDeclaredConstructor().newInstance();
            abilities.put(slot, instance);
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<BaseAbility> getAbilities(Player player) {
        List<BaseAbility> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            BaseAbility ability = getAbility(player, i);
            if (ability != null) slots.add(ability);
        }
        return slots;
    }

    public static void setCooldown(Player player, int slot, int cooldownTicks) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        data.putInt("cooldown_slot_" + slot, cooldownTicks);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static int getCooldown(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        String key = "cooldown_slot_" + slot;
        return data.contains(key) ? data.getInt(key) : 0;
    }

    public static boolean isOnCooldown(Player player, int slot) {
        return getCooldown(player, slot) > 0;
    }

    public static void resetAllCooldowns(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        for (int i = 0; i < 9; i++) {
            data.remove("cooldown_slot_" + i);
        }
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static boolean isAbilityEnabled(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        return data.getBoolean("slot_enabled_" + slot);
    }

    public static void setAbilityEnabled(Player player, int slot, boolean enabled) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        data.putBoolean("slot_enabled_" + slot, enabled);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static void removePlayerAbilities(Player player) {
        playerAbilities.remove(player.getUUID());
    }

    public static void addAmbientAbility(Player player, ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);
        String idString = abilityId.toString();
        for (Tag t : ambientList) {
            if (t != null && t.getAsString().equals(idString)) return;
        }
        ambientList.add(StringTag.valueOf(idString));
        data.put("ambient", ambientList);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static void removeAmbientAbility(Player player, ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);
        ListTag newList = new ListTag();
        String idString = abilityId.toString();
        for (Tag t : ambientList) {
            if (t != null && !t.getAsString().equals(idString)) newList.add(t);
        }
        data.put("ambient", newList);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static List<BaseAbility> getAmbientAbilities(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG).copy();
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);
        List<BaseAbility> abilities = new ArrayList<>();
        for (Tag t : ambientList) {
            if (t == null) continue;
            String raw = t.getAsString();
            if (raw.isEmpty()) continue;
            try {
                ResourceLocation id = ResourceLocation.parse(raw);
                BaseAbility ability = ProvAbilities.ABILITIES.getRegistry().get().get(id);
                if (ability != null) abilities.add(ability);
            } catch (Exception ignored) {
            }
        }
        return abilities;
    }

    public static void applyArchetype(Player player, BaseArchetype archetype) {
        CompoundTag data = new CompoundTag();
        Map<Integer, BaseAbility> slots = archetype.getPlayerAbilities();
        if (slots != null) {
            for (var entry : slots.entrySet()) {
                int slot = entry.getKey();
                BaseAbility ability = entry.getValue();
                if (ability == null) continue;
                try {
                    var key = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
                    if (key != null) data.putString("slot_" + slot, key.toString());
                } catch (Exception ignored) {
                }
            }
        }
        ListTag ambientList = new ListTag();
        List<AmbientAbility> ambientAbilities = archetype.getAmbientAbilities();
        if (ambientAbilities != null) {
            for (AmbientAbility ability : ambientAbilities) {
                if (ability == null) continue;
                try {
                    var id = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
                    if (id != null) ambientList.add(StringTag.valueOf(id.toString()));
                } catch (Exception ignored) {
                }
            }
        }
        data.put("ambient", ambientList);
        data.putFloat("action_points", MAX_AP);
        player.getPersistentData().put(ABILITY_TAG, data);
    }
}
