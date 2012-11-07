package ro.narc.liquiduu;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import net.minecraft.src.Block;
import net.minecraft.src.Item;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

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

    public static Configuration config;

    @SidedProxy(clientSide = "ro.narc.liquiduu.ClientProxy", serverSide = "ro.narc.liquiduu.CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
    }

    @Mod.Init
    public static void init(FMLInitializationEvent event) {

        try {
            config.load();
        }
        catch (RuntimeException e) { /* and ignore it */ }

        Property liquidItemID = config.getItem("liquid.uu", 21001);
        config.save();

        liquidUU = new ItemGeneric(liquidItemID.getInt(21001));
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
