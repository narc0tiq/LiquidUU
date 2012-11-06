package ro.narc.liquiduu;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import net.minecraft.src.Item;

@Mod(
        modid="LiquidUU",
        useMetadata=true,
        dependencies="required-after:IC2;required-after:BuildCraft|Transport; required-after:BuildCraft|Energy"
    )
@NetworkMod(
        clientSideRequired=true
    )
public class LiquidUU {
    public static Item liquidUU;

    @SidedProxy(clientSide = "ro.narc.liquiduu.ClientProxy", serverSide = "ro.narc.liquiduu.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Init
    public static void init(FMLInitializationEvent event) {

        liquidUU = new ItemGeneric(21001);
        liquidUU.setItemName("LiquidUU");
        liquidUU.setIconIndex(0);
        liquidUU.setTextureFile("/liquiduu.png");

        proxy.init(liquidUU);
    }

}
