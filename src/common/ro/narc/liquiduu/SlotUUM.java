package ro.narc.liquiduu;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

import ic2.common.Ic2Items;

public class SlotUUM extends Slot {
    public SlotUUM(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if(stack == null) {
            return false;
        }

        if(stack.isItemEqual(LiquidUU.cannedUU) || stack.isItemEqual(Ic2Items.matter)) {
            return true;
        }
        return false;
    }
}
