package net.mc3699.provenance.util;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.mc3699.provenance.registry.ProvAbilities;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

public class AbilityCommandSuggestions {

    public static final SuggestionProvider<CommandSourceStack> ABILITY_IDS =
            (ctx, builder) -> SharedSuggestionProvider.suggestResource(
            ProvAbilities.ABILITIES.getRegistry().get().keySet(),
            builder
    );

}
