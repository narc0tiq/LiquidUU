package ro.narc.liquiduu;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.LanguageRegistry;

import buildcraft.core.render.TextureLiquidsFX;

import net.minecraft.src.Item;
import net.minecraftforge.client.MinecraftForgeClient;

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

    @Mod.Init
    public static void init(FMLInitializationEvent event) {
        MinecraftForgeClient.preloadTexture("/liquiduu.png");

        liquidUU = new ItemGeneric(21001);
        liquidUU.setItemName("LiquidUU");
        liquidUU.setIconIndex(0);
        liquidUU.setTextureFile("/liquiduu.png");
        LanguageRegistry.addName(liquidUU, "Liquid UU-Matter");

        TextureLiquidsFX liquidUUFX = new TextureLiquidsFX(140, 210, 40, 80, 140, 210, liquidUU.getIconFromDamage(0), liquidUU.getTextureFile());
        liquidUUFX.tileImage = 3595;
        TextureFXManager.instance().addAnimation(liquidUUFX);
    }

}
