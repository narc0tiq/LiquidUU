import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.event.FMLInitializationEvent;


@Mod(
        useMetadata=true,
        dependencies="required-after:IC2;required-after:Buildcraft|Transport"
    )
@NetworkMod(
        clientSideRequired=true
    )
public class LiquidUU {

}
