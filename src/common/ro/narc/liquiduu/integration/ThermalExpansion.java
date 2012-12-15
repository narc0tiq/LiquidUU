package ro.narc.liquiduu.integration;

import ro.narc.liquiduu.LiquidUU;

import ic2.common.Ic2Items;

import thermalexpansion.api.crafting.CraftingManagers;

public class ThermalExpansion {
    public static boolean init() {
        // This should not be reversible (i.e. cannot liquify matter in transposer and get a cell)
        CraftingManagers.transposerManager.addFillRecipe(120,
            Ic2Items.cell, Ic2Items.matter, LiquidUU.liquidUUStack, false);

        // But do allow liquifying matter in general.
        CraftingManagers.transposerManager.addExtractionRecipe(120,
            Ic2Items.matter, null, LiquidUU.liquidUUStack, 0, false);

        return true;
    }
}
