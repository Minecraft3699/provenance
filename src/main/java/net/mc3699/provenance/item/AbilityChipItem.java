package net.mc3699.provenance.item;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ProvenanceRegistries;
import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.registry.ProvComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class AbilityChipItem extends Item {
    public AbilityChipItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(level instanceof ServerLevel serverLevel) {

            ItemStack stack = player.getItemInHand(usedHand);
            BaseAbility itemAbility = getAbility(stack, serverLevel);
            if(itemAbility == null) return InteractionResultHolder.fail(stack);

            ResourceLocation abilityID = stack.get(ProvComponents.ABILITY_ID);

            assert abilityID != null;
            ProvenanceDataHandler.addAbility(player, abilityID);

            serverLevel.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS);
            stack.shrink(1);
        }



        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {

        if (context.level() != null) {

            BaseAbility stackAbility = getAbility(stack, context.level());


            if (stackAbility == null) {
                tooltipComponents.add(Component.literal("ERROR UNKNOWN ABILITY").withStyle(ChatFormatting.DARK_RED));
            } else {
                tooltipComponents.add(stackAbility.getName());
            }


            super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        }
    }

    @Nullable
    public static BaseAbility getAbility(ItemStack stack, Level level) {

        ResourceLocation id = stack.get(ProvComponents.ABILITY_ID);
        if(id == null) return null;

        return level.registryAccess().registryOrThrow(ProvenanceRegistries.ABILITY).get(id);
    }
}
