package ro.narc.liquiduu;

import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import ic2.api.IWrenchable;

public class TileEntityElectrolyzer extends TileEntity implements IWrenchable, IHasMachineFaces {
    public short facing;
    public short prevFacing;
    public MachineFace[] faces = new MachineFace[6];

    public boolean initialized = false;

    public TileEntityElectrolyzer() {
        super();
        this.blockType = LiquidUU.liquidUUBlock;
    }

    public void rotateFaces(short prevFront, short newFront) {
        // TODO: Actually rotate the faces rather than just setting all of them to None except the front.
        for(int i = 0; i < faces.length; i++) {
            faces[i] = MachineFace.None;
        }
        faces[newFront] = MachineFace.ElectrolyzerFront;

        // TODO: resend description packet, hopefully causing the client to redraw us
    }

//public interface IWrenchable {
    public boolean wrenchCanSetFacing(EntityPlayer player, int side) {
        if(side != facing) {
            return true;
        }

        return false;
    }

    public short getFacing() {
        return facing;
    }

    public void setFacing(short side) {
        if(side < 2) { // DOWN or UP -- just ignore them. We cannot face that way, ever.
            return;
        }

        facing = side;

        if(facing != prevFacing) {
            rotateFaces(prevFacing, facing);
            prevFacing = facing;
        }
    }

    public boolean wrenchCanRemove(EntityPlayer player) {
        return true;
    }

    public float getWrenchDropRate() {
        return 1.0F;
    }

    public ItemStack getWrenchDrop(EntityPlayer player) {
        return LiquidUU.electrolyzer;
    }
//}

//public interface IHasMachineFaces {
    public MachineFace getMachineFace(int side) {
        return faces[side];
    }
//}
}
