package ro.narc.liquiduu;

import cpw.mods.fml.relauncher.Side;

import ic2.api.IWrenchable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import ro.narc.util.TileEntityStateful;

public class TileEntityElectrolyzer extends TileEntityStateful implements IWrenchable, IHasMachineFaces {
    public short facing = 3;
    public short prevFacing = 3;
    public MachineFace[] faces = new MachineFace[] {
        MachineFace.OutputEU, MachineFace.InputEU,
        MachineFace.None, MachineFace.ElectrolyzerFront,
        MachineFace.Water, MachineFace.ElectricWater
    };

    public boolean initialized = false;

    public TileEntityElectrolyzer() {
        super();
        this.blockType = LiquidUU.liquidUUBlock;

        this.statePacketID = PacketHandler.PKID_MACHINE_STATE;
        this.channelName = PacketHandler.CHANNEL_NAME;
    }

    public void rotateFaces(short prevFront, short newFront) {
        MachineFace[] newFaces = new MachineFace[6];

        int[] rotationMatrix = MachineFace.getRotationMatrix(prevFront, newFront);

        for(int i = 0; i < faces.length; i++) {
            newFaces[i] = faces[rotationMatrix[i]];
        }
        this.faces = newFaces;
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

            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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

    @Override
    public void readFromNetwork(DataInput data) throws IOException {
        for(int i = 0; i < faces.length; i++) {
            faces[i] = MachineFace.translateOrdinal(data.readByte());
        }
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void writeToNetwork(DataOutput data) throws IOException {
        for(int i = 0; i < faces.length; i++) {
            data.writeByte((byte)faces[i].ordinal());
        }
    }

    // readFrom and writeTo NBT go here.
}
