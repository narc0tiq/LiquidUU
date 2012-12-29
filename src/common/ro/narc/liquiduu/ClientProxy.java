package ro.narc.liquiduu;

import cpw.mods.fml.client.TextureFXManager;

import buildcraft.core.render.TextureLiquidsFX;
import net.minecraftforge.client.MinecraftForgeClient;

import net.minecraft.item.Item;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();

        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/blocks.png");
        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/items.png");

        TextureLiquidsFX liquidUUFX = new TextureLiquidsFX(140, 210, 40, 80, 140, 210,
                LiquidUU.liquidUU.getItem().getIconFromDamage(0),
                LiquidUU.liquidUU.getItem().getTextureFile());
        liquidUUFX.tileImage = 3595;
        TextureFXManager.instance().addAnimation(liquidUUFX);

        TextureFXElectrolyzed electricWaterFX = new TextureFXElectrolyzed(93, 138, 136, 202, 158, 234,
                LiquidUU.electrolyzedWater.getItem().getIconFromDamage(0),
                LiquidUU.electrolyzedWater.getItem().getTextureFile());
        electricWaterFX.tileImage = 3596;
        TextureFXManager.instance().addAnimation(electricWaterFX);
    }
}
