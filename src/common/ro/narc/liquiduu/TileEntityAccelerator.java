package ro.narc.liquiduu;

import cpw.mods.fml.common.Side;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet54PlayNoteBlock;
import net.minecraft.src.TileEntity;

import ic2.api.IWrenchable;
import ic2.common.Ic2Items;
import ic2.common.TileEntityMachine;

public class TileEntityAccelerator extends TileEntityMachine implements IWrenchable {
    public short facing;
    public short prevFacing;

    public boolean initialized = false;

    public TileEntityAccelerator() {
        super(3); // inventory slots
        this.blockType = LiquidUU.liquidUUBlock;
    }

    public void updateEntity() {
        if(!initialized && !isInvalid()) {
            initialize();
        }
    }

    public void initialize() {
        initialized = true;
    }

    public Packet getDescriptionPacket() {
        return new Packet54PlayNoteBlock(xCoord, yCoord, zCoord, LiquidUU.liquidUUBlock.blockID, BlockGeneric.EID_FACING, facing);
    }

    public String getInvName() {
        return "liquiduu.accelerator";
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

        if(LiquidUU.getSide() == Side.CLIENT) {
            return; // Client is a poopyface.
        }

        System.out.println("SetFacing: " + worldObj + ".addBlockEvent(" + xCoord + ", " + yCoord + ", " + zCoord + ", " + blockType + ", " + BlockGeneric.EID_FACING + ", " + facing + ")");

        if(facing != this.prevFacing) {
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, LiquidUU.liquidUUBlock.blockID, BlockGeneric.EID_FACING, facing);
        }
        this.prevFacing = facing;
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
        this.prevFacing = this.facing = tag.getShort("face");
    }

    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort("face", this.facing);
    }
}
