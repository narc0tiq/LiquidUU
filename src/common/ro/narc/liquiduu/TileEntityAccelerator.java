package ro.narc.liquiduu;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;

import ic2.api.IWrenchable;
import ic2.common.TileEntityMachine;

public class TileEntityAccelerator extends TileEntityMachine implements IWrenchable {
    public short facing = 0;

    public TileEntityAccelerator() {
        super(2); // inventory slots
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
        return 1.0f;
    }
}
