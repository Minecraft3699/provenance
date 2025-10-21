package net.mc3699.provenance.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ProvKeymappings {

    public static final KeyMapping ABILITY_BAR_KEY = new KeyMapping(
            "key.provenance.ability_bar",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_1_KEY = new KeyMapping(
            "key.provenance.ability_1",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_2_KEY = new KeyMapping(
            "key.provenance.ability_2",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_3_KEY = new KeyMapping(
            "key.provenance.ability_3",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_4_KEY = new KeyMapping(
            "key.provenance.ability_4",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_5_KEY = new KeyMapping(
            "key.provenance.ability_5",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_6_KEY = new KeyMapping(
            "key.provenance.ability_6",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_7_KEY = new KeyMapping(
            "key.provenance.ability_7",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );

    public static final KeyMapping ABILITY_8_KEY = new KeyMapping(
            "key.provenance.ability_8",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "key.categories.misc"
    );


}
