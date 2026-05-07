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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ProvenanceDataHandler {

    public static final String ABILITY_TAG = "provenance_abilities";
    public static final float MAX_AP = 8.0f;

    private static final Map<UUID, Map<ResourceLocation, BaseAbility>> playerAbilities = new HashMap<>();
    private static final Map<UUID, Map<ResourceLocation, BaseAbility>> playerAmbientAbilities = new HashMap<>();

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

    public static void addAbility(Player player, ResourceLocation id) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag list = data.getList("abilities", Tag.TAG_STRING);
        StringTag tag = StringTag.valueOf(id.toString());
        if (!list.contains(tag)) list.add(tag);
        data.put("abilities", list);
        player.getPersistentData().put(ABILITY_TAG, data);
        playerAbilities.remove(player.getUUID());
    }

    public static void removeAbility(Player player, ResourceLocation id) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag list = data.getList("abilities", Tag.TAG_STRING);
        list.removeIf(t -> t.getAsString().equals(id.toString()));
        data.put("abilities", list);
        player.getPersistentData().put(ABILITY_TAG, data);
        playerAbilities.remove(player.getUUID());
    }

    @Nullable
    public static BaseAbility getAbility(Player player, ResourceLocation id) {
        UUID uuid = player.getUUID();
        Map<ResourceLocation, BaseAbility> map = playerAbilities.computeIfAbsent(uuid, k -> new HashMap<>());

        if (map.containsKey(id)) return map.get(id);

        BaseAbility template = ProvAbilities.ABILITIES.getRegistry().get().get(id);
        if (template == null) return null;

        try {
            BaseAbility instance = template.getClass().getDeclaredConstructor().newInstance();
            map.put(id, instance);
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<BaseAbility> getAbilities(Player player) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag list = data.getList("abilities", Tag.TAG_STRING);
        List<BaseAbility> abilities = new ArrayList<>();

        for (Tag t : list) {
            ResourceLocation id = ResourceLocation.tryParse(t.getAsString());
            if (id == null) continue;
            BaseAbility ability = getAbility(player, id);
            if (ability != null) abilities.add(ability);
        }
        return abilities;
    }

    public static void setCooldown(Player player, ResourceLocation id, int cooldownTicks) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        data.putInt("cooldown_" + id, cooldownTicks);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static int getCooldown(Player player, ResourceLocation id) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        return data.contains("cooldown_" + id) ? data.getInt("cooldown_" + id) : 0;
    }

    public static boolean isOnCooldown(Player player, ResourceLocation id) {
        return getCooldown(player, id) > 0;
    }

    public static boolean isAbilityEnabled(Player player, ResourceLocation id) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        return data.getBoolean("enabled_" + id);
    }

    public static void setAbilityEnabled(Player player, ResourceLocation id, boolean enabled) {
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        data.putBoolean("enabled_" + id, enabled);
        player.getPersistentData().put(ABILITY_TAG, data);
    }

    public static List<BaseAbility> getAmbientAbilities(Player player) {
        UUID uuid = player.getUUID();
        Map<ResourceLocation, BaseAbility> ambientMap = playerAmbientAbilities.computeIfAbsent(uuid, k -> new HashMap<>());

        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag list = data.getList("ambient", Tag.TAG_STRING);
        List<BaseAbility> abilities = new ArrayList<>();

        for (Tag t : list) {
            ResourceLocation id = ResourceLocation.tryParse(t.getAsString());
            if (id == null) continue;

            if (ambientMap.containsKey(id)) {
                abilities.add(ambientMap.get(id));
            } else {
                BaseAbility template = ProvAbilities.ABILITIES.getRegistry().get().get(id);
                if (template == null) continue;
                try {
                    BaseAbility instance = template.getClass().getDeclaredConstructor().newInstance();
                    ambientMap.put(id, instance);
                    abilities.add(instance);
                } catch (Exception ignored) {
                }
            }
        }
        return abilities;
    }

    public static void removePlayerAbilities(Player player) {
        UUID id = player.getUUID();
        playerAbilities.remove(id);
        playerAmbientAbilities.remove(id);
    }

    public static boolean disableAbilityInstance(Player player, BaseAbility instance) {
        for (BaseAbility ability : getAbilities(player)) {
            if (ability == instance) {
                ResourceLocation id = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
                if (id != null) {
                    setAbilityEnabled(player, id, false);
                    return true;
                }
            }
        }
        return false;
    }

    public static void applyArchetype(Player player, BaseArchetype archetype) {
        CompoundTag data = new CompoundTag();
        data.putFloat("action_points", MAX_AP);

        ListTag abilityList = new ListTag();
        if (archetype.getGrantedAbilities() != null) {
            for (ResourceLocation id : archetype.getGrantedAbilities()) {
                abilityList.add(StringTag.valueOf(id.toString()));
            }
        }
        data.put("abilities", abilityList);

        ListTag ambientList = new ListTag();
        if (archetype.getAmbientAbilities() != null) {
            for (AmbientAbility ability : archetype.getAmbientAbilities()) {
                ResourceLocation id = ProvAbilities.ABILITIES.getRegistry().get().getKey(ability);
                if (id != null) {
                    ambientList.add(StringTag.valueOf(id.toString()));
                }
            }
        }
        data.put("ambient", ambientList);

        player.getPersistentData().put(ABILITY_TAG, data);
        removePlayerAbilities(player);
    }

    public static ResourceLocation getIdForAbility(Player player, BaseAbility ability) {
        for (BaseAbility a : getAbilities(player)) {
            if (a == ability) {
                for (ResourceLocation id : getAbilityIds(player)) {
                    BaseAbility resolved = getAbility(player, id);
                    if (resolved == ability) return id;
                }
            }
        }
        return null;
    }

    public static Set<ResourceLocation> getAbilityIds(Player player) {
        Set<ResourceLocation> ids = new HashSet<>();
        CompoundTag data = player.getPersistentData().getCompound(ABILITY_TAG);
        ListTag list = data.getList("abilities", 8);
        for (int i = 0; i < list.size(); i++) {
            String raw = list.getString(i);
            ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id != null) ids.add(id);
        }
        return ids;
    }


}