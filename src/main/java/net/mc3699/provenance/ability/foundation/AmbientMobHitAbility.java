package net.mc3699.provenance.ability.foundation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public abstract class AmbientMobHitAbility extends AmbientAbility {

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }

    @Override
    public void tick(ServerPlayer player) {
        // nuh uh
    }

    public abstract void onEntityHit(ServerPlayer serverPlayer, LivingEntity entity);

    @Override
    public ResourceLocation getIcon() {
        return super.getIcon();
    }
}
