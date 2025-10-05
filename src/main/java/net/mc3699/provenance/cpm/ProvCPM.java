package net.mc3699.provenance.cpm;

import com.tom.cpm.api.ICPMPlugin;
import com.tom.cpm.api.IClientAPI;
import com.tom.cpm.api.ICommonAPI;
import net.mc3699.provenance.Provenance;

public class ProvCPM implements ICPMPlugin {

    public static ICommonAPI serverAPI;
    public static IClientAPI clientAPI;

    @Override
    public void initClient(IClientAPI iClientAPI) {
        clientAPI = iClientAPI;
    }

    @Override
    public void initCommon(ICommonAPI iCommonAPI) {
        serverAPI = iCommonAPI;
    }

    @Override
    public String getOwnerModId() {
        return Provenance.MODID;
    }

}