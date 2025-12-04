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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProvenanceDataHandler {

    public static final String ABILITY_TAG = "provenance_abilities";
    public static final float MAX_AP = 8.0f;

    private static final Map<UUID, Map<Integer, BaseAbility>> playerAbilities = new HashMap<>();

    public static void changeAP(Player player, float amount) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        float existing = data.contains("action_points") ? data.getFloat("action_points") : MAX_AP;
        data.putFloat("action_points", Mth.clamp(existing + amount, 0, MAX_AP));
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static float getAP(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        return data.contains("action_points") ? data.getFloat("action_points") : MAX_AP;
    }

    public static void setAbility(Player player, int slot, @Nullable ResourceLocation abilityId) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        String key = "slot_" + slot;
        if (abilityId != null) data.putString(key, abilityId.toString());
        else data.remove(key);
        player.getPersistentData().put(ABILITY_TAG, data);
        Map<Integer, BaseAbility> abilities = playerAbilities.get(player.getUUID());
        if (abilities != null) abilities.remove(slot);
    }

    @Nullable
    public static ResourceLocation getAbilityId(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        String key = "slot_" + slot;
        if (!data.contains(key)) return null;
        String raw = data.getString(key);
        if (raw.isEmpty()) return null;
        try {
            return ResourceLocation.tryParse(raw);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public static BaseAbility getAbility(Player player, int slot) {
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
        List<BaseAbility> abilities = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            BaseAbility ability = getAbility(player, i);
            if (ability != null) abilities.add(ability);
        }
        return abilities;
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
        for (int i = 0; i < 9; i++) data.remove("cooldown_slot_" + i);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static boolean isAbilityEnabled(Player player, int slot) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        return data.getBoolean("slot_enabled_" + slot);
    }

    public static void setAbilityEnabled(Player player, int slot, boolean enabled) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        data.putBoolean("slot_enabled_" + slot, enabled);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    private static final Map<UUID, Map<ResourceLocation, BaseAbility>> playerAmbientAbilities = new HashMap<>();

    public static List<BaseAbility> getAmbientAbilities(Player player) {
        UUID playerId = player.getUUID();
        Map<ResourceLocation, BaseAbility> ambientMap = playerAmbientAbilities.computeIfAbsent(playerId, k -> new HashMap<>());

        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag ambientList = data.getList("ambient", Tag.TAG_STRING);
        List<BaseAbility> abilities = new ArrayList<>();

        for (Tag t : ambientList) {
            if (t == null) continue;
            String raw = t.getAsString();
            if (raw.isEmpty()) continue;
            try {
                ResourceLocation id = ResourceLocation.tryParse(raw);
                if (id == null) continue;
                if (ambientMap.containsKey(id)) {
                    abilities.add(ambientMap.get(id));
                } else {
                    BaseAbility templateAbility = ProvAbilities.ABILITIES.getRegistry().get().get(id);
                    if (templateAbility != null) {
                        BaseAbility instance = templateAbility.getClass().getDeclaredConstructor().newInstance();
                        ambientMap.put(id, instance);
                        abilities.add(instance);
                    }
                }
            } catch (Exception ignored) {}
        }
        return abilities;
    }

    public static void removePlayerAbilities(Player player) {
        UUID id = player.getUUID();
        playerAbilities.remove(id);
        playerAmbientAbilities.remove(id);
    }

    public static boolean disableAbilityInstance(Player player, BaseAbility instance) {
        for (int slot = 0; slot < 9; slot++) {
            BaseAbility a = getAbility(player, slot);
            if (a == instance) {
                setAbilityEnabled(player, slot, false);
                return true;
            }
        }
        return false;
    }

    public static void applyArchetype(Player player, BaseArchetype archetype) {
        CompoundTag data = new CompoundTag();
        data.putFloat("action_points", MAX_AP);
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
        for (int i = 0; i < 9; i++) {
            data.remove("slot_enabled_" + i);
            data.remove("cooldown_slot_" + i);
        }
        player.getPersistentData().put(ABILITY_TAG, data);
        playerAbilities.remove(player.getUUID());
    }
}
