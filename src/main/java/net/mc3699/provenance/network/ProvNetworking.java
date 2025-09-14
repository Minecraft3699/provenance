package net.mc3699.provenance.network;


import net.mc3699.provenance.Provenance;
import net.mc3699.provenance.ability.utils.AbilityDataHandler;
import net.mc3699.provenance.ability.utils.AbilityExecutor;
import net.mc3699.provenance.ability.utils.ClientAbilityInfo;
import net.mc3699.provenance.handlers.PlayerSyncHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Provenance.MODID)
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

        registrar.playToServer(
                RequestDataSyncPayload.TYPE,
                RequestDataSyncPayload.CODEC,
                PlayerSyncHandler::handleSyncRequests
        );

    }

}
