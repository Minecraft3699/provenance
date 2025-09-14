package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.base.ToggleAbility;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;

public class ToggleTestAbility extends ToggleAbility {

    @Override
    public String getName() {
        return "Toggle Test Ability";
    }

    @Override
    public void tick(ServerPlayer serverPlayer) {
        if(serverPlayer.level() instanceof  ServerLevel serverLevel)
        {
            serverLevel.setBlock(serverPlayer.blockPosition().below(1), Blocks.STONE.defaultBlockState(), 3);
        }
        super.tick(serverPlayer);
    }

    @Override
    public float getUseCost() {
        return 0.05f;
    }
}
