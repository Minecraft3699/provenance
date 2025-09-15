package net.mc3699.provenance.handlers;

import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ProvenanceDataHandler;
import net.mc3699.provenance.ability.foundation.AmbientAbility;
import net.mc3699.provenance.ability.foundation.AmbientMobHitAbility;
import net.mc3699.provenance.ability.foundation.BaseAbility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

@EventBusSubscriber(modid = Provenance.MODID)
public class MobHitAbilityHandler {

    @SubscribeEvent
    public static void onMobHit(LivingDamageEvent.Post event)
    {
        if(event.getSource().getDirectEntity() instanceof ServerPlayer player)
        {
            List<BaseAbility> ambientAbilities = ProvenanceDataHandler.getAmbientAbilities(player);
            for (BaseAbility ability : ambientAbilities) {
                if(ability instanceof AmbientMobHitAbility ambient)
                {
                    if(ambient.canExecute(player))
                    {
                        ambient.onEntityHit(player, event.getEntity());
                    }
                }
            }
        }
    }

}
