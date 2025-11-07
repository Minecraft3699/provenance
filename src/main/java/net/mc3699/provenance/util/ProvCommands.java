package net.mc3699.provenance.util;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.archetype.foundation.BaseArchetype;
import net.mc3699.provenance.network.AbilityDataSyncPayload;
import net.mc3699.provenance.registry.ProvAbilities;
import net.mc3699.provenance.registry.ProvArchetypes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Provenance.MODID)
public class ProvCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("ability").requires(src -> src.hasPermission(2))

                // Add ability
                .then(Commands.literal("set").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("slot", IntegerArgumentType.integer(0, 8)).then(Commands.argument("ability", ResourceLocationArgument.id()).suggests(AbilityCommandSuggestions.ABILITY_IDS).executes(ctx -> {
                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                    int slot = IntegerArgumentType.getInteger(ctx, "slot");
                    ResourceLocation id = ResourceLocationArgument.getId(ctx, "ability");

                    if (!ProvAbilities.ABILITIES.getRegistry().get().containsKey(id)) {
                        ctx.getSource().sendFailure(Component.literal("Unknown ability: " + id));
                        return 0;
                    }

                    ProvenanceDataHandler.setAbility(player, slot, id);
                    ctx.getSource().sendSuccess(() -> Component.literal("Set slot " + slot + " of " + player.getName().getString() + " to " + id), true);
                    return 1;
                })))))

                // Remove ability
                .then(Commands.literal("clear").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("slot", IntegerArgumentType.integer(0, 8)).executes(ctx -> {
                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                    int slot = IntegerArgumentType.getInteger(ctx, "slot");

                    ProvenanceDataHandler.setAbility(player, slot, null);
                    ctx.getSource().sendSuccess(() -> Component.literal("Cleared slot " + slot + " for " + player.getName().getString()), true);
                    if (player instanceof ServerPlayer serverPlayer) {
                        CompoundTag abilities = player.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
                        PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(abilities.copy()));
                    }
                    return 1;
                })))));


        // archetype

        dispatcher.register(Commands.literal("archetype").requires(src -> src.hasPermission(2)).then(Commands.literal("set").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("archetype", ResourceLocationArgument.id()).suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(ProvArchetypes.ARCHETYPES.getRegistry().get().keySet(), builder)).executes(ctx -> {
            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
            ResourceLocation id = ResourceLocationArgument.getId(ctx, "archetype");

            BaseArchetype archetype = ProvArchetypes.ARCHETYPES.getRegistry().get().get(id);
            if (archetype == null) {
                ctx.getSource().sendFailure(Component.literal("Unknown archetype: " + id));
                return 0;
            }

            ProvenanceDataHandler.applyArchetype(player, archetype);
            if (player instanceof ServerPlayer serverPlayer) {
                CompoundTag abilities = player.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
                PacketDistributor.sendToPlayer(serverPlayer, new AbilityDataSyncPayload(abilities.copy()));
            }

            ctx.getSource().sendSuccess(() -> Component.literal("Assigned archetype " + archetype.getName().getString() + " to " + player.getName().getString()), true);
            return 1;
        })))));
    }
}
