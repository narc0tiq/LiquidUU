package ro.narc.liquiduu;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import net.minecraft.src.Block;
import net.minecraft.src.Item;

import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.recipes.RefineryRecipe;
import buildcraft.BuildCraftEnergy;

import ic2.common.Ic2Items;

@Mod(
        modid="LiquidUU",
        version="0.5",
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

        LiquidStack liquidUUStack = new LiquidStack(liquidUU, 1000);
        LiquidData liquidUUData = new LiquidData(liquidUUStack, Ic2Items.matter,
                Ic2Items.cell);
        LiquidManager.liquids.add(liquidUUData);

        RefineryRecipe.registerRefineryRecipe(new RefineryRecipe(new LiquidStack(liquidUU, 1), new LiquidStack(Block.waterStill.blockID, 1), new LiquidStack(Block.waterStill.blockID, 10), 5, 1));
        RefineryRecipe.registerRefineryRecipe(new RefineryRecipe(new LiquidStack(liquidUU, 1), new LiquidStack(Block.lavaStill.blockID, 1), new LiquidStack(Block.lavaStill.blockID, 5), 5, 1));
        RefineryRecipe.registerRefineryRecipe(new RefineryRecipe(new LiquidStack(liquidUU, 2), new LiquidStack(BuildCraftEnergy.fuel.shiftedIndex, 1), new LiquidStack(BuildCraftEnergy.fuel.shiftedIndex, 2), 5, 1));
    }

}
