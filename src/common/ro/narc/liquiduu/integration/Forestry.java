package ro.narc.liquiduu.integration;

import ro.narc.liquiduu.LiquidUU;

import buildcraft.api.liquids.LiquidData;
import buildcraft.api.liquids.LiquidManager;

import forestry.api.core.ItemInterface;
import forestry.api.recipes.RecipeManagers;

import ic2.common.Ic2Items;

import net.minecraft.src.ItemStack;

// ItemInterface.getItem("canEmpty")
public class Forestry {
    public static boolean init() {

        ItemStack canEmpty = ItemInterface.getItem("canEmpty");
        ItemStack canUU    = new ItemStack(LiquidUU.cannedUU);

        RecipeManagers.bottlerManager.addRecipe(5, LiquidUU.liquidUUStack, Ic2Items.cell, Ic2Items.matter);
        RecipeManagers.bottlerManager.addRecipe(5, LiquidUU.liquidUUStack, canEmpty, canUU);

        LiquidData cannedUUData = new LiquidData(LiquidUU.liquidUUStack, canUU, canEmpty);
        LiquidManager.liquids.add(cannedUUData);

        return true;
    }
}
