package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Items;

public class BurgerAbility extends BaseAbility {

    @Override
    public void execute(ServerPlayer player) {
        player.eat(player.serverLevel(), Items.COOKED_BEEF.getDefaultInstance(), Foods.COOKED_BEEF);
        super.execute(player);
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return true;
    }

    @Override
    public Component getName() {
        return Component.literal("Burger");
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
