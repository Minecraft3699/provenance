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

import java.util.Collection;

@EventBusSubscriber(modid = Provenance.MODID)
public class ProvCommands {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("ability")
                        .requires(src -> src.hasPermission(2))

                        .then(Commands.literal("add")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("ability", ResourceLocationArgument.id())
                                                .suggests(AbilityCommandSuggestions.ABILITY_IDS)
                                                .executes(ctx -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                                    ResourceLocation id = ResourceLocationArgument.getId(ctx, "ability");

                                                    if (!ProvAbilities.ABILITIES.getRegistry().get().containsKey(id)) {
                                                        ctx.getSource().sendFailure(Component.literal("Unknown ability: " + id));
                                                        return 0;
                                                    }

                                                    ProvenanceDataHandler.addAbility(player, id);
                                                    syncData(player);

                                                    ctx.getSource().sendSuccess(
                                                            () -> Component.literal("Added ability " + id + " to " + player.getName().getString()),
                                                            true
                                                    );
                                                    return 1;
                                                }))))

                        .then(Commands.literal("remove")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("ability", ResourceLocationArgument.id())
                                                .suggests(AbilityCommandSuggestions.ABILITY_IDS)
                                                .executes(ctx -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                                    ResourceLocation id = ResourceLocationArgument.getId(ctx, "ability");

                                                    ProvenanceDataHandler.removeAbility(player, id);
                                                    syncData(player);

                                                    ctx.getSource().sendSuccess(
                                                            () -> Component.literal("Removed ability " + id + " from " + player.getName().getString()),
                                                            true
                                                    );
                                                    return 1;
                                                }))))
        );

        dispatcher.register(
                Commands.literal("archetype")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("set")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("archetype", ResourceLocationArgument.id())
                                                .suggests((ctx, builder) ->
                                                        SharedSuggestionProvider.suggestResource(
                                                                ProvArchetypes.ARCHETYPES.getRegistry().get().keySet(),
                                                                builder
                                                        ))
                                                .executes(ctx -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                                    ResourceLocation id = ResourceLocationArgument.getId(ctx, "archetype");

                                                    BaseArchetype archetype =
                                                            ProvArchetypes.ARCHETYPES.getRegistry().get().get(id);

                                                    if (archetype == null) {
                                                        ctx.getSource().sendFailure(Component.literal("Unknown archetype: " + id));
                                                        return 0;
                                                    }

                                                    ProvenanceDataHandler.applyArchetype(player, archetype);
                                                    syncData(player);

                                                    ctx.getSource().sendSuccess(
                                                            () -> Component.literal(
                                                                    "Assigned archetype " +
                                                                            archetype.getName().getString() +
                                                                            " to " +
                                                                            player.getName().getString()
                                                            ),
                                                            true
                                                    );
                                                    return 1;
                                                }))))
        );

        dispatcher.register(
                Commands.literal("pxp")
                        .requires(src -> src.hasPermission(2))

                        .then(Commands.literal("add")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");

                                                    for (ServerPlayer target : targets) {
                                                        ProvenanceDataHandler.addXP(target, amount);
                                                        syncData(target);
                                                    }

                                                    if (targets.size() == 1) {
                                                        ServerPlayer target = targets.iterator().next();
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Added " + amount + " XP to " + target.getName().getString()),
                                                                true
                                                        );
                                                    } else {
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Added " + amount + " XP to " + targets.size() + " players"),
                                                                true
                                                        );
                                                    }
                                                    return targets.size();
                                                }))))

                        .then(Commands.literal("remove")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                .executes(ctx -> {
                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                    int amount = IntegerArgumentType.getInteger(ctx, "amount");

                                                    for (ServerPlayer target : targets) {
                                                        ProvenanceDataHandler.addXP(target, -amount);
                                                        syncData(target);
                                                    }

                                                    if (targets.size() == 1) {
                                                        ServerPlayer target = targets.iterator().next();
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Removed " + amount + " XP from " + target.getName().getString()),
                                                                true
                                                        );
                                                    } else {
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Removed " + amount + " XP from " + targets.size() + " players"),
                                                                true
                                                        );
                                                    }
                                                    return targets.size();
                                                }))))

                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                                .executes(ctx -> {
                                                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(ctx, "targets");
                                                    int newXp = IntegerArgumentType.getInteger(ctx, "amount");

                                                    for (ServerPlayer target : targets) {
                                                        int currentXp = ProvenanceDataHandler.getXP(target);
                                                        int delta = newXp - currentXp;
                                                        ProvenanceDataHandler.addXP(target, delta);
                                                        syncData(target);
                                                    }

                                                    if (targets.size() == 1) {
                                                        ServerPlayer target = targets.iterator().next();
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Set XP for " + target.getName().getString() + " to " + newXp),
                                                                true
                                                        );
                                                    } else {
                                                        ctx.getSource().sendSuccess(
                                                                () -> Component.literal("Set XP for " + targets.size() + " players to " + newXp),
                                                                true
                                                        );
                                                    }
                                                    return targets.size();
                                                }))))

                        .then(Commands.literal("get")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> {
                                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                                            int xp = ProvenanceDataHandler.getXP(player);

                                            ctx.getSource().sendSuccess(
                                                    () -> Component.literal(player.getName().getString() + " has " + xp + " XP"),
                                                    false
                                            );
                                            return xp;
                                        })))
        );
    }

    private static void syncData(ServerPlayer player) {
        CompoundTag abilities = player.getPersistentData().getCompound(ProvenanceDataHandler.ABILITY_TAG);
        PacketDistributor.sendToPlayer(player, new AbilityDataSyncPayload(abilities.copy()));
    }
}