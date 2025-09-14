package net.mc3699.provenance.ability.abilities;

import net.mc3699.provenance.ability.base.BaseAbility;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class EmptyAbility extends BaseAbility {

    @Override
    public void execute(ServerPlayer player) {
        // do nothing
    }

    @Override
    public String getName() {
        return "Empty Slot";
    }

    @Override
    public boolean canExecute(ServerPlayer player) {
        return false;
    }

    @Override
    public ResourceLocation getIcon() {
        return ProvConstants.path("textures/ability/empty.png");
    }
}
