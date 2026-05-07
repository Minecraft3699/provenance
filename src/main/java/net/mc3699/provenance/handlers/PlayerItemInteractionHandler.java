package net.mc3699.provenance.handlers;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.ability.foundation.foodOverride.FoodOverrideAbility;
import net.mc3699.provenance.ability.foundation.foodOverride.FoodOverrideData;
import net.mc3699.provenance.registry.ProvComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber
public class PlayerItemInteractionHandler {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack stack = event.getItemStack();
        List<BaseAbility> ambients = ProvenanceDataHandler.getAmbientAbilities(player);

        for (BaseAbility ability : ambients) {
            if (ability instanceof FoodOverrideAbility diet) {
                FoodOverrideData override = diet.getFoodOverrides().get(stack.getItem());

                if (override != null) {
                    if (!player.canEat(false)) {
                        return;
                    }

                    FoodProperties.Builder builder = new FoodProperties.Builder()
                            .nutrition((int) override.nutrition())
                            .saturationModifier(override.saturation());

                    for (MobEffectInstance effect : override.effects()) {
                        builder.effect(effect, 1.0f);
                    }

                    stack.set(ProvComponents.PROVENANCE_MODIFIED.get(), true);
                    stack.set(DataComponents.FOOD, builder.build());

                    player.inventoryMenu.broadcastChanges();
                    player.startUsingItem(event.getHand());

                    event.setCancellationResult(InteractionResult.CONSUME);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof ServerPlayer) {
            cleanup(event.getItem());
        }
    }

    @SubscribeEvent
    public static void onUseStop(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity() instanceof ServerPlayer) {
            cleanup(event.getItem());
        }
    }

    private static void cleanup(ItemStack stack) {
        if (stack.has(ProvComponents.PROVENANCE_MODIFIED.get())) {
            stack.remove(ProvComponents.PROVENANCE_MODIFIED.get());
            FoodProperties original = stack.getItem().components().get(DataComponents.FOOD);

            if (original != null) {
                stack.set(DataComponents.FOOD, original);
            } else {
                stack.remove(DataComponents.FOOD);
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if (stack.has(ProvComponents.PROVENANCE_MODIFIED.get())) {
            cleanup(stack);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            cleanup(player.getMainHandItem());
            cleanup(player.getOffhandItem());
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack usingStack = player.getUseItem();

            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getItem(i);

                if (stack.has(ProvComponents.PROVENANCE_MODIFIED.get()) && stack != usingStack) {
                    cleanup(stack);
                }
            }

            ItemStack offhand = player.getOffhandItem();
            if (offhand.has(ProvComponents.PROVENANCE_MODIFIED.get()) && offhand != usingStack) {
                cleanup(offhand);
            }
        }
    }
}