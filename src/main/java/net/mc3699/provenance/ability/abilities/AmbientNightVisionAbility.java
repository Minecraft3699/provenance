package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class AmbientNightVisionAbility extends AmbientAbility {

    private int timer = 0;

    @Override
    public void tick(ServerPlayer player) {
        this.timer++;
        if(this.timer % 20 == 0)
        {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 240, 1, true, false), player);
            this.timer = 0;
        }
    }

    @Override
    public Component getName() {
        return Component.literal("Night Vision");
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }
}
