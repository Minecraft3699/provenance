package net.mc3699.provenance.abilityMenu.keybind;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.mc3699.provenance.Provenance;
import net.minecraft.resources.ResourceLocation;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class AbilityKeybindHandler {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config", "provenance_ability_keybinds.json");

    private static final Map<ResourceLocation, Map<ResourceLocation, AbilityKeybind>> ALL_BINDINGS = new HashMap<>();

    private static ResourceLocation currentArchetype = Provenance.path("human");

    private static boolean assigning = false;
    private static ResourceLocation assigningAbility = null;

    static {
        load();
    }

    public static void setCurrentArchetype(ResourceLocation archetype) {
        currentArchetype = archetype;
    }

    public static ResourceLocation getCurrentArchetype() {
        return currentArchetype;
    }

    private static Map<ResourceLocation, AbilityKeybind> getActiveBindings() {
        if (currentArchetype == null) {
            currentArchetype = Provenance.path("human");
        }
        return ALL_BINDINGS.computeIfAbsent(currentArchetype, k -> new HashMap<>());
    }

    public static boolean isAssigning() {
        return assigning;
    }

    public static ResourceLocation getAssigningAbility() {
        return assigningAbility;
    }

    public static void beginAssign(ResourceLocation id) {
        assigning = true;
        assigningAbility = id;
    }

    public static void finishAssign() {
        assigning = false;
        assigningAbility = null;
    }

    public static Set<ResourceLocation> getAllBoundAbilities() {
        return Collections.unmodifiableSet(getActiveBindings().keySet());
    }

    public static void setBinding(ResourceLocation id, AbilityKeybind binding) {
        getActiveBindings().put(id, binding);
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());

            Map<String, Map<String, AbilityKeybind>> serializableMap = new HashMap<>();

            for (Map.Entry<ResourceLocation, Map<ResourceLocation, AbilityKeybind>> archEntry : ALL_BINDINGS.entrySet()) {
                Map<String, AbilityKeybind> abilityMap = new HashMap<>();
                for (Map.Entry<ResourceLocation, AbilityKeybind> abEntry : archEntry.getValue().entrySet()) {
                    abilityMap.put(abEntry.getKey().toString(), abEntry.getValue());
                }
                serializableMap.put(archEntry.getKey().toString(), abilityMap);
            }

            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(serializableMap, writer);
            }
        } catch (Exception e) {
            System.err.println("[Provenance] Failed to save ability keybinds: " + e.getMessage());
        }
    }

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Type type = new TypeToken<Map<String, Map<String, AbilityKeybind>>>() {}.getType();
            Map<String, Map<String, AbilityKeybind>> loadedMap = GSON.fromJson(reader, type);

            ALL_BINDINGS.clear();

            if (loadedMap != null) {
                for (Map.Entry<String, Map<String, AbilityKeybind>> archEntry : loadedMap.entrySet()) {
                    ResourceLocation archetypeId = ResourceLocation.tryParse(archEntry.getKey());
                    if (archetypeId == null) continue;

                    Map<ResourceLocation, AbilityKeybind> abilityMap = new HashMap<>();
                    for (Map.Entry<String, AbilityKeybind> abEntry : archEntry.getValue().entrySet()) {
                        ResourceLocation abilityId = ResourceLocation.tryParse(abEntry.getKey());
                        if (abilityId != null && abEntry.getValue() != null) {
                            abilityMap.put(abilityId, abEntry.getValue());
                        }
                    }
                    ALL_BINDINGS.put(archetypeId, abilityMap);
                }
            }
        } catch (Exception e) {
            System.err.println("[Provenance] Failed to load ability keybinds: " + e.getMessage());
        }
    }

    public static AbilityKeybind getBinding(ResourceLocation id) {
        return getActiveBindings().get(id);
    }

    public static boolean matches(ResourceLocation id, int key, int modifiers) {
        if (assigning) return false;
        AbilityKeybind bind = getActiveBindings().get(id);
        if (bind == null) return false;
        return bind.keyCode() == key;
    }

    public static void clearBinding(ResourceLocation id) {
        if (getActiveBindings().remove(id) != null) {
            save();
        }
    }

    public static void clearAllBindings() {
        getActiveBindings().clear();
        save();
    }
}