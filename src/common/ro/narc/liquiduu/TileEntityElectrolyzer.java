package ro.narc.liquiduu;

import cpw.mods.fml.relauncher.Side;

import ic2.api.IWrenchable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;

import ro.narc.util.TileEntityStateful;

public class TileEntityElectrolyzer extends TileEntityStateful implements IWrenchable, IHasMachineFaces {
    public short facing = 3;
    public short prevFacing = 3;
    public MachineFace[] faces = new MachineFace[] {
        MachineFace.OutputEU, MachineFace.InputEU,
        MachineFace.None, MachineFace.ElectrolyzerIdle,
        MachineFace.Water, MachineFace.ElectricWater
    };

    public boolean initialized = false;

    public TileEntityElectrolyzer() {
        super();
        this.blockType = CommonProxy.machineBlock;

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
        return CommonProxy.electrolyzer.copy();
    }
//}

//public interface IHasMachineFaces {
    public MachineFace getMachineFace(int side) {
        return faces[side];
    }
//}

    // Protected because getConnectedTanks always returns the same List<T>, which is unexpected behaviour for anyone using the output
    protected List<ILiquidTank> workList = new ArrayList<ILiquidTank>();
    protected List<ILiquidTank> getConnectedTanks(MachineFace connectingFace) {
        workList.clear();

        for(ForgeDirection dir: ForgeDirection.VALID_DIRECTIONS) {
            if(getMachineFace(dir.ordinal()) != connectingFace) {
                continue;
            }

            TileEntity te = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if(!(te instanceof ITankContainer)) {
                continue;
            }

            ILiquidTank[] arr = ((ITankContainer)te).getTanks(ForgeDirection.UNKNOWN);
            for(int i = 0; i < arr.length; i++) {
                if(!workList.contains(arr[i])) {
                    workList.add(arr[i]);
                }
            }
        }

        return workList;
    }

    public LiquidStack getConnectedLiquid(LiquidStack desiredLiquid, MachineFace connectingFace) {
        LiquidStack ls = desiredLiquid.copy(); ls.amount = 0;
        List<ILiquidTank> liquidTanks = getConnectedTanks(connectingFace);

        for(ILiquidTank tank: liquidTanks) {
            LiquidStack tankLiquid = tank.getLiquid();
            if(ls.isLiquidEqual(tankLiquid)) {
                ls.amount += tankLiquid.amount;
            }
        }

        return ls;
    }

    public int getConnectedLiquidCapacity(LiquidStack desiredLiquid, MachineFace connectingFace) {
        int capacity = 0;
        List<ILiquidTank> liquidTanks = getConnectedTanks(connectingFace);

        for(ILiquidTank tank: liquidTanks) {
            LiquidStack tankLiquid = tank.getLiquid();
            if((tankLiquid == null) || desiredLiquid.isLiquidEqual(tankLiquid)) {
                capacity += tank.getCapacity();
            }
        }

        return capacity;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
            return false;
        }

        if(player.getDistance((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) > 64.0D) {
            return false;
        }
        return true;
    }

    public void getGUINetworkData(int key, int value) {
        switch(key) {
            default:
                System.err.println("LiquidUU: Electrolyzer got unknown GUI network data for key " + key + ": " + value);
                break;
        }
    }

    public void sendGUINetworkData(ContainerElectrolyzer container, ICrafting iCrafting) {
        //iCrafting.sendProgressBarUpdate(container, 0, tank.getLiquidAmount());
    }

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
