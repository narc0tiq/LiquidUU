package ro.narc.liquiduu.integration;

import ro.narc.liquiduu.LiquidUU;

import forestry.api.recipes.RecipeManagers;

import ic2.common.Ic2Items;

public class Forestry {
    public static boolean init() {
        RecipeManagers.bottlerManager.addRecipe(5, LiquidUU.liquidUUStack, Ic2Items.cell, Ic2Items.matter);

        return true;
    }
}
