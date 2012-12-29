package ro.narc.liquiduu;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import buildcraft.api.recipes.RefineryRecipe;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftTransport;

import ic2.core.Ic2Items;

@Mod(
        modid="LiquidUU",
        version="%conf:VERSION%",
        useMetadata=true,
        dependencies="required-after:IC2;required-after:BuildCraft|Transport;required-after:BuildCraft|Energy;required-after:BuildCraft|Factory;after:Forestry"
    )
@NetworkMod(
        clientSideRequired = true,
        versionBounds = "%conf:VERSION_BOUNDS%"
    )
public class LiquidUU {
    public static boolean DEBUG_NETWORK = false;

    public static ItemStack liquidUU;
    public static LiquidStack liquidUUStack;
    public static ItemStack cannedUU;
    public static Block liquidUUBlock;
    public static ItemStack accelerator;
    public static ItemStack electrolyzer;
    public static ItemStack electrolyzedWater;
    public static LiquidStack electrolyzedWaterStack;

    @Mod.Instance("LiquidUU")
    public static LiquidUU instance;

    public static Configuration config;

    @SidedProxy(clientSide = "ro.narc.liquiduu.ClientProxy", serverSide = "ro.narc.liquiduu.CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {

        try {
            config.load();
        }
        catch (RuntimeException e) { /* and ignore it */ }

        Property accelBlockID = config.getBlock("accelerator", 1300);
        Property liquidItemID = config.getItem("liquid.uu", 21001);
        Property cannedItemID = config.getItem("canned.uu", 21002);
        Property electricWaterID = config.getItem("electricWater", 21003);
        config.save();

        Item liquidUUItem = new ItemGeneric("liquidUU", 0, liquidItemID.getInt(21001));
        liquidUU = new ItemStack(liquidUUItem, 1);

        Item cannedUUItem = new ItemGeneric("cannedUU", 1, cannedItemID.getInt(21002));
        cannedUU = new ItemStack(cannedUUItem, 1);

        Item liquidElectrolyzedWaterItem = new ItemGeneric("electricWater", 2, electricWaterID.getInt(21003));
        electrolyzedWater = new ItemStack(liquidElectrolyzedWaterItem, 1);

        liquidUUBlock = new BlockGeneric(accelBlockID.getInt(1300));
        liquidUUBlock.setCreativeTab(ic2.core.IC2.tabIC2);
        GameRegistry.registerBlock(liquidUUBlock, ItemGenericBlock.class, liquidUUBlock.getBlockName());
        GameRegistry.registerTileEntity(TileEntityAccelerator.class, "Accelerator");
        GameRegistry.registerTileEntity(TileEntityElectrolyzer.class, "liquiduu.Electrolyzer");
        accelerator  = new ItemStack(liquidUUBlock, 1, BlockGeneric.DATA_ACCELERATOR);
        electrolyzer = new ItemStack(liquidUUBlock, 1, BlockGeneric.DATA_ELECTROLYZER);

        GameRegistry.addRecipe(accelerator, "TH", "SA", " w",
                Character.valueOf('T'), BuildCraftFactory.tankBlock,
                Character.valueOf('H'), BuildCraftFactory.hopperBlock,
                Character.valueOf('S'), BuildCraftTransport.pipeLiquidsStone,
                Character.valueOf('A'), Ic2Items.advancedCircuit,
                Character.valueOf('w'), BuildCraftTransport.pipeItemsWood
        );
        GameRegistry.addRecipe(accelerator, " ST", "wAH",
                Character.valueOf('T'), BuildCraftFactory.tankBlock,
                Character.valueOf('H'), BuildCraftFactory.hopperBlock,
                Character.valueOf('S'), BuildCraftTransport.pipeLiquidsStone,
                Character.valueOf('A'), Ic2Items.advancedCircuit,
                Character.valueOf('w'), BuildCraftTransport.pipeItemsWood
        );
        GameRegistry.addRecipe(accelerator, "wAH", " ST",
                Character.valueOf('T'), BuildCraftFactory.tankBlock,
                Character.valueOf('H'), BuildCraftFactory.hopperBlock,
                Character.valueOf('S'), BuildCraftTransport.pipeLiquidsStone,
                Character.valueOf('A'), Ic2Items.advancedCircuit,
                Character.valueOf('w'), BuildCraftTransport.pipeItemsWood
        );

        proxy.init();

        initLiquidData();
        initRefineryRecipes();

        NetworkRegistry.instance().registerGuiHandler(this, new LiquidUUGUIHandler());
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
        loadIntegration("Forestry");
        loadIntegration("NEI");
        loadIntegration("ThermalExpansion");
    }

    @SuppressWarnings("unchecked")
    private static void initLiquidData() {
        liquidUUStack = new LiquidStack(liquidUU.getItem(), 1000);
        liquidUUStack = LiquidDictionary.getOrCreateLiquid("liquidUU", liquidUUStack);
        LiquidContainerData liquidUUData = new LiquidContainerData(liquidUUStack, Ic2Items.matter,
                Ic2Items.cell);
        LiquidContainerRegistry.registerLiquid(liquidUUData);

        electrolyzedWaterStack = new LiquidStack(electrolyzedWater.getItem(), 1000);
        electrolyzedWaterStack = LiquidDictionary.getOrCreateLiquid("electrolyzedWater", electrolyzedWaterStack);
        LiquidContainerData electricWaterData = new LiquidContainerData(electrolyzedWaterStack,
                Ic2Items.electrolyzedWaterCell, Ic2Items.cell);
        LiquidContainerRegistry.registerLiquid(electricWaterData);
    }

    public static Side getSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    private static void initRefineryRecipes() {
        Item liquidUUItem = liquidUU.getItem();

        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUUItem, 1),
                new LiquidStack(Block.waterStill.blockID, 1),
                new LiquidStack(Block.waterStill.blockID, 10),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUUItem, 1),
                new LiquidStack(Block.lavaStill.blockID, 1),
                new LiquidStack(Block.lavaStill.blockID, 5),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUUItem, 1),
                new LiquidStack(BuildCraftEnergy.oilStill.blockID, 1),
                new LiquidStack(BuildCraftEnergy.oilStill.blockID, 2),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUUItem, 2),
                new LiquidStack(BuildCraftEnergy.fuel.shiftedIndex, 1),
                new LiquidStack(BuildCraftEnergy.fuel.shiftedIndex, 2),
                5, 1
            )
        );
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
            e.printStackTrace();
            return false;
        }
    }
}
