package ro.narc.liquiduu.integration;

import ro.narc.liquiduu.CommonProxy;

import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import buildcraft.api.recipes.RefineryRecipe;

import forestry.api.core.ItemInterface;
import forestry.api.recipes.RecipeManagers;

import ic2.core.Ic2Items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Forestry {
    public static boolean init() {

        ItemStack canEmpty = ItemInterface.getItem("canEmpty");
        ItemStack ingotTin = ItemInterface.getItem("ingotTin");
        CommonProxy.cannedUUItemStack.getItem().setCreativeTab(canEmpty.getItem().getCreativeTab());

        RecipeManagers.squeezerManager.addRecipe(5, new ItemStack[]{Ic2Items.matter}, CommonProxy.liquidUULiquidStack);
        RecipeManagers.squeezerManager.addRecipe(5, new ItemStack[]{CommonProxy.cannedUUItemStack},
                CommonProxy.liquidUULiquidStack, ingotTin, 5);

        RecipeManagers.bottlerManager.addRecipe(5, CommonProxy.liquidUULiquidStack, Ic2Items.cell, Ic2Items.matter);
        RecipeManagers.bottlerManager.addRecipe(5, CommonProxy.liquidUULiquidStack, canEmpty, CommonProxy.cannedUUItemStack);

        initLiquidContainers(canEmpty);
        initRefineryRecipes();

        return true;
    }

    @SuppressWarnings("unchecked")
    public static void initLiquidContainers(ItemStack canEmpty) {
        LiquidContainerData cannedUUData = new LiquidContainerData(CommonProxy.liquidUULiquidStack,
                CommonProxy.cannedUUItemStack, canEmpty);
        LiquidContainerRegistry.registerLiquid(cannedUUData);
    }

    public static void initRefineryRecipes() {
        Item liquidBiomass = ItemInterface.getItem("liquidBiomass").getItem();
        Item liquidBiofuel = ItemInterface.getItem("liquidBiofuel").getItem();
        Item liquidSeedOil = ItemInterface.getItem("liquidSeedOil").getItem();
        Item liquidJuice   = ItemInterface.getItem("liquidJuice").getItem();
        Item liquidHoney   = ItemInterface.getItem("liquidHoney").getItem();
        Item crushedIce    = ItemInterface.getItem("liquidIce").getItem();
        Item liquidUU      = CommonProxy.liquidUUItemStack.getItem();

        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 1),
                new LiquidStack(liquidBiomass, 1),
                new LiquidStack(liquidBiomass, 1 + CommonProxy.biomassConversion),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 2),
                new LiquidStack(liquidBiofuel, 1),
                new LiquidStack(liquidBiofuel, 1 + CommonProxy.biofuelConversion),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 1),
                new LiquidStack(liquidSeedOil, 1),
                new LiquidStack(liquidSeedOil, 1 + CommonProxy.seedOilConversion),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 1),
                new LiquidStack(liquidJuice, 1),
                new LiquidStack(liquidJuice, 1 + CommonProxy.appleJuiceConversion),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 1),
                new LiquidStack(liquidHoney, 1),
                new LiquidStack(liquidHoney, 1 + CommonProxy.honeyConversion),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(liquidUU, 1),
                new LiquidStack(crushedIce, 1),
                new LiquidStack(crushedIce, 1 + CommonProxy.iceConversion),
                5, 1
            )
        );
    }
}
