package ro.narc.liquiduu;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import ic2.core.Ic2Items;

public class SlotUUM extends Slot {
    public SlotUUM(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack == null) {
            return false;
        }

        if(stack.isItemEqual(CommonProxy.cannedUUItemStack) || stack.isItemEqual(Ic2Items.matter)) {
            return true;
        }
        return false;
    }
}
