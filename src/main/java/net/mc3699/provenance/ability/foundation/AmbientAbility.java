package net.mc3699.provenance.ability.foundation;

import net.minecraft.server.level.ServerPlayer;

public abstract class AmbientAbility extends BaseAbility {

    public abstract void tick(ServerPlayer player);

    @Override
    public float getUseCost() {
        return 0;
    }
}
