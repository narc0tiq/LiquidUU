package ro.narc.liquiduu;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityLiquidUU extends TileEntity {
    @Override
    public Packet getDescriptionPacket() {
        return (new PacketMachineState(this)).getPacket250();
    }

    public abstract void readFromNetwork(DataInput data) throws IOException;
    public abstract void writeToNetwork(DataOutput data) throws IOException;
}
