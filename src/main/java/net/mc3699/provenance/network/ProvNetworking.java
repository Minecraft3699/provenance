package net.mc3699.provenance.network;


import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.AbilityExecutor;
import net.mc3699.provenance.ability.ClientAbilityInfo;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Provenance.MODID)
public class ProvNetworking {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event)
    {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                TriggerAbilityPayload.TYPE,
                TriggerAbilityPayload.STREAM_CODEC,
                AbilityExecutor::runAbility
        );

        registrar.playToClient(
                AbilityDataSyncPayload.TYPE,
                AbilityDataSyncPayload.CODEC,
                ClientAbilityInfo::handle
        );

    }

}
