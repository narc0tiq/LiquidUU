package ro.narc.liquiduu;

import cpw.mods.fml.common.Side;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.EntityPlayer;

public class BlockGeneric extends BlockContainer {
    public static final byte DATA_ACCELERATOR = 0;

    public BlockGeneric(int id, Material material) {
        super(id, material);
    }

    public BlockGeneric(int id) {
        this(id, Material.iron);
    }

    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    public TileEntity createNewTileEntity(World world, int data) {
        return new TileEntityAccelerator();
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hit_x, float hit_y, float hit_z) {
        if(player.isSneaking()) {
            return false;
        }
        if(LiquidUU.getSide() == Side.CLIENT) {
            return true;
        }

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if(!(te instanceof TileEntityAccelerator)) {
            return false;
        }
        // Nothing to do with the tileentity yet.

        return true;
    }
}
