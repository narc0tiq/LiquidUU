package ro.narc.liquiduu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot {
    public ItemStack displayStack = null;

    public SlotOutput(IInventory inventory, int slotIndex, int xPos, int yPos) {
        super(inventory, slotIndex, xPos, yPos);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack getStack() {
       if((super.getStack() != null) && (displayStack != null)) {
            displayStack.stackSize = super.getStack().stackSize;
            return displayStack;
        }
        return super.getStack();
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if((displayStack != null) && (stack != null) && (displayStack.isItemEqual(stack))) {
            int stackSize = stack.stackSize;
            if(super.getStack() != null) {
                stack = super.getStack().copy();
            }
            stack.stackSize = stackSize;
        }

        super.onPickupFromSlot(player, stack);
    }
}
