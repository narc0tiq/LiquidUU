package ro.narc.liquiduu;

import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

import java.io.DataInput;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {
    public static final String CHANNEL_NAME = "liquidUU";
    public static final byte PKID_MACHINE_STATE = 0;

    public void onPacketData(INetworkManager network, Packet250CustomPayload packet, Player player) {
        DataInput data = ByteStreams.newDataInput(packet.data);

        try {
            byte packetID = data.readByte();

            if(packetID == PKID_MACHINE_STATE) {
                PacketMachineState.readPacket250(data);
            }
            else {
                System.err.println("Narc, did you forget to add packet handling for packet ID " + packetID + "?");
            }
        }
        catch(IOException e) {
            // and pretend it never existed.
        }
    }
}
