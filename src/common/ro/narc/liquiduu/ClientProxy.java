package ro.narc.liquiduu;

import buildcraft.core.render.TextureLiquidsFX;

import cpw.mods.fml.client.TextureFXManager;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(Configuration config) {
        super.init(config);

        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/blocks.png");
        MinecraftForgeClient.preloadTexture("/liquiduu-gfx/items.png");

        TextureLiquidsFX liquidUUFX = new TextureLiquidsFX(140, 210, 40, 80, 140, 210,
                liquidUUItemStack.getItem().getIconFromDamage(0),
                liquidUUItemStack.getItem().getTextureFile());
        liquidUUFX.tileImage = 3595;
        TextureFXManager.instance().addAnimation(liquidUUFX);

        TextureFXElectrolyzed electricWaterFX = new TextureFXElectrolyzed(93, 138, 136, 202, 178, 234,
                electricWaterItemStack.getItem().getIconFromDamage(0),
                electricWaterItemStack.getItem().getTextureFile());
        electricWaterFX.tileImage = 3596;
        TextureFXManager.instance().addAnimation(electricWaterFX);
    }
}
