package ro.narc.liquiduu.integration;

import ro.narc.liquiduu.LiquidUU;

import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;
import buildcraft.api.recipes.RefineryRecipe;

import forestry.api.core.ItemInterface;
import forestry.api.recipes.RecipeManagers;

import ic2.common.Ic2Items;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class Forestry {
    public static boolean init() {

        ItemStack canEmpty = ItemInterface.getItem("canEmpty");
        ItemStack canUU    = new ItemStack(LiquidUU.cannedUU);
        Item liquidBiomass = ItemInterface.getItem("liquidBiomass").getItem();
        Item liquidBiofuel = ItemInterface.getItem("liquidBiofuel").getItem();
        Item liquidSeedOil = ItemInterface.getItem("liquidSeedOil").getItem();
        Item liquidJuice   = ItemInterface.getItem("liquidJuice").getItem();
        Item liquidHoney   = ItemInterface.getItem("liquidHoney").getItem();

        RecipeManagers.bottlerManager.addRecipe(5, LiquidUU.liquidUUStack, Ic2Items.cell, Ic2Items.matter);
        RecipeManagers.bottlerManager.addRecipe(5, LiquidUU.liquidUUStack, canEmpty, canUU);

        LiquidData cannedUUData = new LiquidData(LiquidUU.liquidUUStack, canUU, canEmpty);
        LiquidManager.liquids.add(cannedUUData);

        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(LiquidUU.liquidUU, 1),
                new LiquidStack(liquidBiomass, 1),
                new LiquidStack(liquidBiomass, 2),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(LiquidUU.liquidUU, 2),
                new LiquidStack(liquidBiofuel, 1),
                new LiquidStack(liquidBiofuel, 2),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(LiquidUU.liquidUU, 1),
                new LiquidStack(liquidSeedOil, 1),
                new LiquidStack(liquidSeedOil, 4),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(LiquidUU.liquidUU, 1),
                new LiquidStack(liquidJuice, 1),
                new LiquidStack(liquidJuice, 4),
                5, 1
            )
        );
        RefineryRecipe.registerRefineryRecipe(
            new RefineryRecipe(
                new LiquidStack(LiquidUU.liquidUU, 1),
                new LiquidStack(liquidHoney, 1),
                new LiquidStack(liquidHoney, 2),
                5, 1
            )
        );

        return true;
    }
}
