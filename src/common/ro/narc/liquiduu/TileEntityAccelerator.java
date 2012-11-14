package ro.narc.liquiduu;

import cpw.mods.fml.common.Side;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet54PlayNoteBlock;
import net.minecraft.src.TileEntity;

import ic2.api.IWrenchable;
import ic2.common.Ic2Items;
import ic2.common.TileEntityMachine;
import ic2.common.TileEntityElectricMachine;
import ic2.common.TileEntityRecycler;

public class TileEntityAccelerator extends TileEntityMachine implements IWrenchable {
    public short facing;
    public short prevFacing;

    public boolean initialized = false;

    public int uu = 1024;

    public TileEntityAccelerator() {
        super(3); // inventory slots
        this.blockType = LiquidUU.liquidUUBlock;
    }

    public void updateEntity() {
        if(isInvalid()) {
            return;
        }

        if(!initialized) {
            initialize();
        }
    }

    public void initialize() {
        initialized = true;
    }

    public Packet getDescriptionPacket() {
        return new Packet54PlayNoteBlock(xCoord, yCoord, zCoord, LiquidUU.liquidUUBlock.blockID, BlockGeneric.EID_FACING, facing);
    }

    /* Facings: YNeg=0; YPos=1; ZNeg=2; ZPos=3; XNeg=4; XPos=5 */
    public TileEntity getConnectedMachine() {
        int x = xCoord;
        int y = yCoord;
        int z = zCoord;

        switch(facing) {
            case 0:
                y--; break;
            case 1:
                y++; break;
            case 2:
                z--; break;
            case 3:
                z++; break;
            case 4:
                x--; break;
            case 5:
                x++; break;
        }

        return worldObj.getBlockTileEntity(x, y, z);
    }

    public ItemStack getConnectedMachineItem() {
        TileEntity te = getConnectedMachine();
        int machineId = worldObj.getBlockId(te.xCoord, te.yCoord, te.zCoord);
        Block machineBlock = Block.blocksList[machineId];

        return machineBlock.getPickBlock(null, worldObj, te.xCoord, te.yCoord, te.zCoord);
    }

    public InstantRecipe getActiveRecipe() {
        TileEntity te = getConnectedMachine();

        InstantRecipe recipe = null;

        if(inventory[0] == null) {
            return null;
        }

        if (te instanceof IAcceleratorFriend) {
            IAcceleratorFriend pal = (IAcceleratorFriend) te;

            if (!pal.instantReady(inventory[0])) {
                // Fine, fine, we won't force you to.
                return null;
            }

            recipe = pal.getInstantRecipe(inventory[0]);
        }
        else if (te instanceof TileEntityElectricMachine) {
            if (te instanceof TileEntityRecycler) {
                // We don't do garbage duty.
                return null;
            }

            TileEntityElectricMachine machine = (TileEntityElectricMachine) te;

            ItemStack input = inventory[0].copy();
            ItemStack result = machine.getResultFor(input, true);

            // Get the true input stack size.
            input.stackSize = inventory[0].stackSize - input.stackSize;

            int cost = (machine.defaultEnergyConsume * machine.defaultOperationLength
                        * machine.defaultOperationLength) / 10000;

            if (cost == 0) {
                cost = 1;
            }

            recipe = new InstantRecipe(input, result, cost);
        }

        if (recipe.validate()) {
            return recipe;
        }

        return null;
    }

    public void consumeUU(int amount) {
        uu -= amount;
    }

    public int batchesPossible() {
        return batchesPossible(getActiveRecipe());
    }

    // Protected because it's assumed that activeRecipe == this.getActiveRecipe().
    protected int batchesPossible(InstantRecipe activeRecipe) {
        if (activeRecipe == null) {
            // Do what, now?
            return 0;
        }

        if (inventory[1] != null) {
            if (!activeRecipe.output.isItemEqual(inventory[1])) {
                return 0;
            }
        }

        int count = inventory[0].stackSize / activeRecipe.getInputSize();

        count = Math.min(count, this.uu / activeRecipe.uuCost);

        if (activeRecipe.machine != null) {
            count = Math.min(count, activeRecipe.machine.instantCapacity(activeRecipe, count));
        }

        int max_additional;
        if (inventory[1] != null) {
            max_additional = inventory[1].getMaxStackSize() - inventory[1].stackSize;
        }
        else {
            max_additional = activeRecipe.output.getMaxStackSize();
        }

        count = Math.min(count, max_additional/activeRecipe.getOutputSize());

        return Math.max(0, count);
    }

    // Protected because it's assumed that activeRecipe == this.getActiveRecipe() and that
    // batches <= batchesPossible(activeRecipe)
    protected void process(InstantRecipe activeRecipe, int batches) {
        if (batches <= 0) {
            return;
        }

        consumeUU(batches * activeRecipe.uuCost);
        if (activeRecipe.machine != null) {
            activeRecipe.machine.instantProcess(activeRecipe, batches);
        }

        inventory[0].stackSize -= (activeRecipe.getInputSize() * batches);
        if (inventory[0].stackSize <= 0) {
            inventory[0] = null;
        }

        if (inventory[1] != null) {
            inventory[1].stackSize += activeRecipe.getOutputSize() * batches;
        } else {
            inventory[1] = activeRecipe.output.copy();
            inventory[1].stackSize *= batches;
        }
    }

    public void attemptProcessing() {
        InstantRecipe recipe = getActiveRecipe();
        int count = batchesPossible(recipe);
        process(recipe, count);
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

        if(facing != this.prevFacing) {
            if(LiquidUU.DEBUG_NETWORK) {
                System.out.println("SetFacing: " + worldObj + ".addBlockEvent(" + xCoord + ", " + yCoord + ", " + zCoord + ", " + LiquidUU.liquidUUBlock.blockID + ", " + BlockGeneric.EID_FACING + ", " + facing + ")");
            }
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
        return stack.isItemEqual(Ic2Items.matter) || stack.isItemEqual(LiquidUU.cannedUU);
    }

    // We have a negative-indexed slot, so we need to override these IInventory bits:
    public synchronized ItemStack getStackInSlot(int slotnum)
    {
        if(slotnum < -1) {
            return null;
        }
        else if(slotnum == -1) {
            return getConnectedMachineItem();
        }
        else if (slotnum == 1) {
            ItemStack output = null;

            if(inventory[1] != null) {
                output = inventory[1].copy();
            }

            InstantRecipe recipe = getActiveRecipe();
            int batches = batchesPossible(recipe);

            if (batches <= 0) {
                return output;
            }

            if(output == null) {
                output = recipe.output.copy();
                output.stackSize *= batches;
            }
            else {
                output.stackSize += recipe.output.stackSize * batches;
            }

            return output;
        }
        return super.getStackInSlot(slotnum);
    }

    public synchronized ItemStack decrStackSize(int slotnum, int amount)
    {
        if (slotnum < 0) {
            return null;
        }
        else if (slotnum == 1) {
            System.out.println("Side " + LiquidUU.getSide() + " decrStackSize("+slotnum+","+amount+")");
            // I just know I'm going to look at this code later and think up a cleaner way to
            //write the same damned thing. For now, this will do (if it works). --Narc
            if(amount == 0) {
                return null;
            }

            ItemStack output = null;
            if(inventory[1] != null) {
                output = inventory[1].copy();
            }

            InstantRecipe recipe = getActiveRecipe();
            int batches = batchesPossible(recipe);

            if (batches <= 0) {
                output.stackSize = Math.min(amount, output.stackSize);
                inventory[1].stackSize -= output.stackSize;

                if(inventory[1].stackSize == 0) {
                    inventory[1] = null;
                }

                return output;
            }

            int amount_ready = 0;
            if(inventory[1] != null) {
                amount_ready = inventory[1].stackSize;
            }

            float batches_requested = ((float) amount - amount_ready) / recipe.getOutputSize();
            batches = Math.min(batches, MathHelper.ceiling_float_int(batches_requested));
            process(recipe, batches); // puts its output into inventory[1]

            if(inventory[1] == null) { // probably shouldn't happen, but if it does...
                return null;
            }

            output = inventory[1].copy();
            if(inventory[1].stackSize > amount) {
                inventory[1].stackSize -= amount;
                output.stackSize = amount;
            }
            else {
                inventory[1] = null;
            }

            return output;
        }
        return super.decrStackSize(slotnum, amount);
    }

    public synchronized void setInventorySlotContents(int slotnum, ItemStack itemstack) {
        if(slotnum < 0 || slotnum == 1) {
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
