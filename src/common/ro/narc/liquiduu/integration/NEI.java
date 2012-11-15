package ro.narc.liquiduu.integration;

import java.util.List;

import codechicken.nei.api.API;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.VisiblityData;

import net.minecraft.src.ItemStack;
import net.minecraft.src.GuiContainer;

import ro.narc.liquiduu.GUIAccelerator;

public class NEI implements INEIGuiHandler {
    public NEI() {}

    public static boolean init() {
        API.registerNEIGuiHandler((INEIGuiHandler) new NEI());

        return true;
    }

    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData visibility) {
        if(!(gui instanceof GUIAccelerator)) {
            return visibility;
        }

        if(visibility.showItemPanel) {
            visibility.showItemPanel = false;
        }

        return visibility;
    }

    public int getItemSpawnSlot(GuiContainer gui, ItemStack item) {
        return -1;
    }

    public List getInventoryAreas(GuiContainer gui) {
      return null;
    }

}
