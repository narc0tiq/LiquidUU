package ro.narc.liquiduu;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;

import net.minecraftforge.common.Configuration;

import ro.narc.util.NarcLib;

@Mod(
        modid="LiquidUU",
        version="%conf:VERSION%",
        useMetadata=true,
        dependencies="required-after:IC2;required-after:BuildCraft|Transport;required-after:BuildCraft|Energy;required-after:BuildCraft|Factory;after:Forestry;after:ThermalExpansion;before:ThermalExpansion|Factory"
)
@NetworkMod(
        clientSideRequired = true,
        versionBounds = "%conf:VERSION_BOUNDS%",
        channels = { PacketHandler.CHANNEL_NAME },
        packetHandler = PacketHandler.class
)
public class LiquidUU {
    public static boolean DEBUG = false;

    @Mod.Instance("LiquidUU")
    public static LiquidUU instance;

    public static Configuration config;

    @SidedProxy(clientSide = "ro.narc.liquiduu.ClientProxy", serverSide = "ro.narc.liquiduu.CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        NarcLib.requireMinVersion(1);
        config = new Configuration(event.getSuggestedConfigurationFile());
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        try {
            config.load();
        }
        catch (RuntimeException e) { /* and ignore it */ }

        proxy.init(config);
        config.save();

        if(DEBUG) {
            NarcLib.enableDebug();
        }
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
        loadIntegration("Forestry");
        loadIntegration("NEI");
        loadIntegration("ThermalExpansion");
        loadIntegration("Railcraft");
    }

    public static Side getSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    @SuppressWarnings("unchecked")
    private static boolean loadIntegration(String name) {
        System.out.println("LiquidUU: Loading " + name + " integration...");

        try {
            Class t = LiquidUU.class.getClassLoader().loadClass("ro.narc.liquiduu.integration." + name);
            return ((Boolean)t.getMethod("init", new Class[0]).invoke((Object)null, new Object[0])).booleanValue();
        }
        catch (Throwable e) {
            System.out.println("LiquidUU: Did not load " + name + " integration: " + e);
            if(DEBUG) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
