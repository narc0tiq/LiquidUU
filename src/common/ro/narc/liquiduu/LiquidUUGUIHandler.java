package ro.narc.liquiduu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;

public class LiquidUUGUIHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof TileEntityAccelerator) {
            return new ContainerAccelerator(player.inventory, (TileEntityAccelerator)te);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world,
            int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof TileEntityAccelerator) {
            return new GUIAccelerator(player.inventory, (TileEntityAccelerator)te);
        }

        return null;
    }
}
