package net.mc3699.provenance.abilityMenu.keybind;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class AbilityKeybindHandler {

    private static final Map<ResourceLocation, AbilityKeybind> BINDINGS = new HashMap<>();

    private static boolean assigning = false;
    private static ResourceLocation assigningAbility = null;

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
        return Collections.unmodifiableSet(BINDINGS.keySet());
    }

    public static void setBinding(ResourceLocation id, AbilityKeybind binding) {
        BINDINGS.put(id, binding);
        save();
    }

    public static AbilityKeybind getBinding(ResourceLocation id) {
        return BINDINGS.get(id);
    }

    public static boolean matches(ResourceLocation id, int key, int modifiers) {
        if (assigning) return false;
        AbilityKeybind bind = BINDINGS.get(id);
        if (bind == null) return false;
        return bind.keyCode() == key;
    }

    public static void clearBinding(ResourceLocation id) {
        if (BINDINGS.remove(id) != null) {
            save();
        }
    }

    private static void save() {
    }

    public static void load() {
    }
}
