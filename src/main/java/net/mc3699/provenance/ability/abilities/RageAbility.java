package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class RageAbility extends BaseAbility {
    @Override
    public float getUseCost() {
        return 4;
    }

    @Override
    public Component getName() {
        return Component.literal("Rage").withStyle(ChatFormatting.DARK_RED);
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }

    @Override
    public void execute(ServerPlayer player) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, true, false));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, true, false));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2, true, false));

        player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.WOLF_GROWL, SoundSource.MASTER, 4,1);

        super.execute(player);
    }

    @Override
    public ResourceLocation getIcon() {
        return ProvConstants.path("textures/ability/rage.png");
    }
}
