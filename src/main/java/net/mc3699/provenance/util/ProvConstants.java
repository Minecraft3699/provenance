package net.mc3699.provenance.util;

import net.mc3699.provenance.Provenance;
import net.minecraft.resources.ResourceLocation;

public class ProvConstants {

    public static ResourceLocation path(String location)
    {
        return ResourceLocation.fromNamespaceAndPath(Provenance.MODID, location);
    }

}
