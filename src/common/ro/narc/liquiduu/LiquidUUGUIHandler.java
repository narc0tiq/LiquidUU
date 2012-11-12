package ro.narc.liquiduu;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.minecraft.src.TileEntity;

import cpw.mods.fml.common.network.IGuiHandler;

public class LiquidUUGUIHandler implements IGuiHandler {
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof TileEntityAccelerator) {
            return new ContainerAccelerator(player.inventory, (TileEntityAccelerator)te);
        }

        return null;
    }

    public Object getClientGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof TileEntityAccelerator) {
            return new GUIAccelerator(player.inventory, (TileEntityAccelerator)te);
        }

        return null;
    }
}
