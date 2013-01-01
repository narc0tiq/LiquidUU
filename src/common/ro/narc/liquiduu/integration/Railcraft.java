package ro.narc.liquiduu.integration;

import buildcraft.api.recipes.RefineryRecipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.liquids.LiquidStack;

import railcraft.common.api.core.items.ItemRegistry;

import ro.narc.liquiduu.LiquidUU;
import ro.narc.liquiduu.CommonProxy;

public class Railcraft {
    public static boolean init() {
        if(LiquidUU.DEBUG) {
            ItemRegistry.printItemTags();
        }

        ItemStack creosoteStack = ItemRegistry.getItem("liquid.creosote.liquid", 1);
        if(creosoteStack != null) {
            Item creosote = creosoteStack.getItem();
            Item liquidUU = CommonProxy.liquidUUItemStack.getItem();

            RefineryRecipe.registerRefineryRecipe(new RefineryRecipe(
                new LiquidStack(liquidUU, 2), new LiquidStack(creosote, 1),
                new LiquidStack(creosote, 1 + CommonProxy.creosoteConversion),
                5, 1
            ));
        }

        return true;
    }
}
