package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.AmbientMobHitAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.fixes.ItemStackTagFix;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;

public class AmbientEatEntityAbility extends AmbientMobHitAbility {
    @Override
    public void onEntityHit(ServerPlayer player, LivingEntity entity) {
        if(entity instanceof Animal)
        {
            player.eat(player.serverLevel(), ItemStack.EMPTY, Foods.MUTTON);
            entity.hurt(player.serverLevel().damageSources().mobAttack(player), 4);
            player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.WOLF_GROWL, SoundSource.PLAYERS, 0.2f,1);
            ProvenanceDataHandler.changeAP(player, 0.5f);
        }
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return !player.hasItemInSlot(EquipmentSlot.MAINHAND);
    }

    @Override
    public Component getName() {
        return Component.literal("Eat living animals");
    }
}
