package net.mc3699.provenance.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.checkerframework.checker.units.qual.K;
import org.jline.keymap.KeyMap;
import org.lwjgl.glfw.GLFW;

public class ProvKeymappings {

    public static final KeyMapping ABILITY_BAR_KEY = new KeyMapping(
            "key.provenance.ability",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.provenance"
    );

    public static final KeyMapping SKILL_TREE_KEY = new KeyMapping(
            "key.provenance.skill_tree",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.provenance"
    );


}
