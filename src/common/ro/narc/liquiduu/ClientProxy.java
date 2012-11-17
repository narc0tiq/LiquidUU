package ro.narc.liquiduu;

import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.registry.LanguageRegistry;

import buildcraft.core.render.TextureLiquidsFX;
import net.minecraftforge.client.MinecraftForgeClient;

import net.minecraft.src.Item;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/blocks.png");
        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/items.png");

        LanguageRegistry.addName(LiquidUU.liquidUU.getItem(), "Liquid UU-Matter");
        LanguageRegistry.addName(LiquidUU.cannedUU.getItem(), "UU-Matter Can");
        LanguageRegistry.addName(LiquidUU.liquidUUBlock, "Uninitialized LiquidUU block");
        LanguageRegistry.addName(LiquidUU.accelerator, "Accelerator");

        TextureLiquidsFX liquidUUFX = new TextureLiquidsFX(140, 210, 40, 80, 140, 210, 
                LiquidUU.liquidUU.getItem().getIconFromDamage(0), 
                LiquidUU.liquidUU.getItem().getTextureFile());
        liquidUUFX.tileImage = 3595;
        TextureFXManager.instance().addAnimation(liquidUUFX);
    }
}
