package net.mc3699.provenance.item;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.abilityItem.IAbilityItem;
import net.mc3699.provenance.network.TriggerAbilityPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class TestAbilityItem extends Item implements IAbilityItem {
    public TestAbilityItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<ResourceLocation> inventoryAbilities() {
        return List.of(
                Provenance.path("test")
        );
    }

    @Override
    public void tick() {

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        PacketDistributor.sendToServer(new TriggerAbilityPayload(inventoryAbilities().getFirst()));
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }
}
