package net.mc3699.provenance.ability.foundation;

import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.util.ProvConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public abstract class BaseAbility {

    public abstract float getUseCost();

    public int getCooldown() { return 1;};

    public abstract Component getName();

    public abstract void execute(ServerPlayer player);

    public abstract boolean canExecute(ServerPlayer player);

    public void backgroundTick(ServerPlayer serverPlayer) {}

    public int getColor() {
        return 0xFFFF00FF;
    }

    public Supplier<Object> getRenderSupplier() {
        return null;
    }

    public Object getClientRenderer() {
        if(FMLEnvironment.dist == Dist.CLIENT && getRenderSupplier() != null) {
            return getRenderSupplier().get();
        }
        return null;
    }

    public ResourceLocation getIcon()
    {
        return ProvConstants.path("textures/ability/missing.png");
    }

}
