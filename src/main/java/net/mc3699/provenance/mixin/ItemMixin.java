package net.mc3699.provenance.mixin;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.foundation.abilityItem.IAbilityItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin implements IAbilityItem {

    Item self = (Item) (Object) this;

    @Override
    public List<ResourceLocation> inventoryAbilities() {
        if (self == Items.COD)
        {
            return List.of(Provenance.path("cod_rain"));
        }
        return List.of();
    }
}
