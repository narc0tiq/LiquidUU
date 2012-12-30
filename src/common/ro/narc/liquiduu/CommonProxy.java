package ro.narc.liquiduu;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.world.World;

public class CommonProxy {
    public void init() {
        LanguageRegistry.addName(LiquidUU.liquidUU, "Liquid UU-Matter");
        LanguageRegistry.addName(LiquidUU.cannedUU, "UU-Matter Can");
        LanguageRegistry.addName(LiquidUU.electrolyzedWater, "Electrolyzed Water");
        LanguageRegistry.addName(LiquidUU.liquidUUBlock, "Uninitialized LiquidUU block");
        LanguageRegistry.addName(LiquidUU.accelerator, "Accelerator");
        LanguageRegistry.addName(LiquidUU.electrolyzer, "Liquid Electrolyzer");
    }

    public World getClientWorld() {
        return null;
    }
}
