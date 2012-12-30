package ro.narc.liquiduu;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import java.io.DataInput;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

public class PacketMachineState {
    public TileEntityLiquidUU machine;

    public PacketMachineState(TileEntityLiquidUU machine) {
        this.machine = machine;
    }

    public Packet getPacket250() {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();

        try {
            data.writeByte(PacketHandler.PKID_MACHINE_STATE);
            data.writeInt(machine.xCoord);
            data.writeInt(machine.yCoord);
            data.writeInt(machine.zCoord);
            machine.writeToNetwork(data);
        }
        catch(IOException e) {
            return null; // shit happens, deal with it
        }

        Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = PacketHandler.CHANNEL_NAME;
        packet.data = data.toByteArray();
        packet.length = packet.data.length;
        packet.isChunkDataPacket = true;

        return packet;
    }

    public static void readPacket250(DataInput data) {
        try {
            int x = data.readInt();
            int y = data.readInt();
            int z = data.readInt();

            World world = LiquidUU.instance.proxy.getClientWorld();
            assert world != null: "The world was null! You are not on the client, but you got a client-side packet!";

            TileEntityLiquidUU machine = (TileEntityLiquidUU)world.getBlockTileEntity(x, y, z);
            machine.readFromNetwork(data);
        }
        catch(IOException e) {
            // and completely ignore it.
        }
    }
}
