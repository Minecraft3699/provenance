package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.base.BaseAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Items;

public class FoodAbility extends BaseAbility {

    @Override
    public void execute(ServerPlayer player) {
        player.eat(player.serverLevel(), Items.COOKED_BEEF.getDefaultInstance(), Foods.COOKED_BEEF);
        super.execute(player);
    }

    @Override
    public String getName() {
        return "Burger";
    }

    @Override
    public ResourceLocation getIcon() {
        return ProvConstants.path("textures/ability/burger.png");
    }

    @Override
    public float getUseCost() {
        return 2f;
    }
}
