package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.ToggleAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.phys.Vec3;

public class CodRainAbility extends ToggleAbility {

    private int timer;

    @Override
    public void execute(ServerPlayer player) {
        timer = 0;
        super.execute(player);
    }

    @Override
    public boolean canExecute(ServerPlayer player){
        return true;
    }

    @Override
    public void tick(ServerPlayer serverPlayer) {
        timer++;
        if(timer % 2 == 0)
        {
            ServerLevel serverLevel = serverPlayer.serverLevel();
            for(int i = 0; i < 50; i++)
            {
                int codSpawnX = serverLevel.random.nextInt(-60,60);
                int codSpawnZ = serverLevel.random.nextInt(-60,60);

                Cod newCod = new Cod(EntityType.COD, serverLevel);
                Vec3 playerPos = serverPlayer.position();
                newCod.setPos(playerPos.x+codSpawnX, playerPos.y+40,playerPos.z+codSpawnZ);
                serverLevel.addFreshEntity(newCod);
            }
        }

        super.tick(serverPlayer);
    }

    @Override
    public void backgroundTick(ServerPlayer serverPlayer) {

    }

    @Override
    public float getUseCost() {
        return 0.05f;
    }

    @Override
    public ResourceLocation getIcon() {
        return ProvConstants.path("textures/ability/cod_rain.png");
    }

    @Override
    public Component getName() {
        return Component.literal("Cod Rain");
    }

    @Override
    public String getAnimation() {
        return "cod_rain";
    }
}
