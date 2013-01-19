package ro.narc.liquiduu.integration;

import cpw.mods.fml.common.Loader;

import ic2.core.Ic2Items;

import net.minecraftforge.liquids.LiquidContainerData;

import ro.narc.liquiduu.CommonProxy;

import thermalexpansion.api.crafting.CraftingManagers;
import thermalexpansion.api.crafting.ITransposerRecipe;

public class ThermalExpansion {
    public static boolean init() {
        // Allow liquifying matter...
        CraftingManagers.transposerManager.addExtractionRecipe(120,
            Ic2Items.matter, null, CommonProxy.liquidUULiquidStack, 0, false);
        // ...and make it possible to re-solidify it by using an empty cell.
        CraftingManagers.transposerManager.addFillRecipe(120,
            Ic2Items.cell, Ic2Items.matter, CommonProxy.liquidUULiquidStack, false);

        if(Loader.isModLoaded("Forestry")) {
            try {
                LiquidContainerData cannedUUData = ro.narc.liquiduu.integration.Forestry.cannedUUData;
                CraftingManagers.transposerManager.addFillRecipe(120,
                    cannedUUData.container, cannedUUData.filled, cannedUUData.stillLiquid, true);
            }
            catch(Exception e) {
                System.out.println("LiquidUU: ThermalExpansion: Could not add Forestry UUM can to Liquid Transposer recipes." + e);
                if(CommonProxy.isDebugging()) {
                    e.printStackTrace();
                }
            }
        }

        if(CommonProxy.isDebugging()) {
            for(ITransposerRecipe recipe: CraftingManagers.transposerManager.getFillRecipeList()) {
                System.out.println("LiquidUU: TE fills " + recipe.getInput() + " into " + recipe.getOutput());
            }
            for(ITransposerRecipe recipe: CraftingManagers.transposerManager.getExtractionRecipeList()) {
                System.out.println("LiquidUU: TE drains " + recipe.getInput() + " into " + recipe.getOutput());
            }
        }

        return true;
    }
}
