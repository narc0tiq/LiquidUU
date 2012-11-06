
package ro.narc.liquiduu;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.registry.LanguageRegistry;

import buildcraft.core.render.TextureLiquidsFX;
import net.minecraftforge.client.MinecraftForgeClient;

import net.minecraft.src.Item;

public class ClientProxy extends CommonProxy {
    public void init(Item liquidUU) {
        MinecraftForgeClient.preloadTexture("/liquiduu.png");

        LanguageRegistry.addName(liquidUU, "Liquid UU-Matter");

        TextureLiquidsFX liquidUUFX = new TextureLiquidsFX(140, 210, 40, 80, 140, 210, liquidUU.getIconFromDamage(0), liquidUU.getTextureFile());
        liquidUUFX.tileImage = 3595;
        TextureFXManager.instance().addAnimation(liquidUUFX);
    }
}
