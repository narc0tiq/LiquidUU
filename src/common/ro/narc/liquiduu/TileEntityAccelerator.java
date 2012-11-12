package ro.narc.liquiduu;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import ic2.api.IWrenchable;
import ic2.common.Ic2Items;
import ic2.common.TileEntityMachine;

public class TileEntityAccelerator extends TileEntityMachine implements IWrenchable {
    public short facing;

    public TileEntityAccelerator() {
        super(3); // inventory slots
    }

    public String getInvName() {
        return "Accelerator";
    }

    public boolean wrenchCanSetFacing(EntityPlayer player, int side) {
        if(this.facing != side) {
            return true;
        }
        return false;
    }

    public short getFacing() {
        return this.facing;
    }

    public void setFacing(short facing) {
        this.facing = facing;
    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return true;
    }

    public float getWrenchDropRate() {
        return 1.0F;
    }

    public boolean isItemStackUUM(ItemStack stack) {
        return ((stack.itemID == Ic2Items.matter.itemID) || 
                (stack.itemID == LiquidUU.cannedUU.shiftedIndex));
    }

    // We have a negative-indexed slot, so we need to override these IInventory bits:
    public synchronized ItemStack getStackInSlot(int slotnum)
    {
        if (slotnum < 0) {
            return null;
        }
        return super.getStackInSlot(slotnum);
    }

    public synchronized ItemStack decrStackSize(int slotnum, int j)
    {
        if (slotnum < 0) {
            return null;
        }
        return super.decrStackSize(slotnum, j);
    }

    public synchronized void setInventorySlotContents(int slotnum, ItemStack itemstack) {
        if(slotnum < 0) {
            return;
        }
        super.setInventorySlotContents(slotnum, itemstack);
    }

    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
    }
}
