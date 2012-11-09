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
        System.out.println("Hey, uh, this guy wants to know if he can change my facing.");
        if(this.facing != side) {
            return true;
        }
        return false;
    }

    public short getFacing() {
        System.out.println("This guy wants my facing, I'll tell him " + this.facing);
        return this.facing;
    }

    public void setFacing(short facing) {
        System.out.println("So that guy now wants me to face " + facing);
        this.facing = facing;
    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        System.out.println("This guy's full of questions. Now he wants to know if he can remove me.");
        return true;
    }

    public float getWrenchDropRate() {
        System.out.println("Drop rate. I get tired of writing this shit.");
        return 1.0f;
    }

    public void bitchAtMe() {
        System.out.println("Yo, bitch.");
    }
}
