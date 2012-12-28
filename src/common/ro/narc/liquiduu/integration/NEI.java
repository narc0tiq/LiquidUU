package ro.narc.liquiduu.integration;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import codechicken.nei.api.API;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.VisiblityData;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import ro.narc.liquiduu.GUIAccelerator;
import ro.narc.liquiduu.LiquidUU;

public class NEI implements INEIGuiHandler {
    public NEI() {}

    public static boolean init() {
        if(LiquidUU.getSide() == Side.CLIENT) {
            API.registerNEIGuiHandler((INEIGuiHandler) new NEI());
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData visibility) {
        if(!(gui instanceof GUIAccelerator)) {
            return visibility;
        }

        if(visibility.showItemPanel) {
            visibility.showItemPanel = false;
        }

        return visibility;
    }

    @SideOnly(Side.CLIENT)
    public int getItemSpawnSlot(GuiContainer gui, ItemStack item) {
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public List getInventoryAreas(GuiContainer gui) {
        return null;
    }

}
