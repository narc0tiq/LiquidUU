package ro.narc.liquiduu;

import cpw.mods.fml.common.Side;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.Random;

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
        if(data == DATA_ACCELERATOR) {
            return new TileEntityAccelerator();
        }

        return null;
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

        player.openGui(LiquidUU.instance, 0, world, x, y, z);
        return true;
    }

    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, par5, par6);
    }

    private void dropItems(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if(!(te instanceof IInventory)) {
            return;
        }

        Random rand = new Random();
        IInventory inventory = (IInventory) te;

        for(int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if((item == null) || item.stackSize <= 0) {
                continue;
            }

            float rx = rand.nextFloat() * 0.8F + 0.1F;
            float ry = rand.nextFloat() * 0.8F + 0.1F;
            float rz = rand.nextFloat() * 0.8F + 0.1F;

            EntityItem toDrop = new EntityItem(world, x + rx, y + ry, z + rz,
                    new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

            if(item.hasTagCompound()) {
                toDrop.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
            }

            float factor = 0.05F;
            toDrop.motionX = rand.nextGaussian() * factor;
            toDrop.motionY = rand.nextGaussian() * factor + 0.2F;
            toDrop.motionZ = rand.nextGaussian() * factor;

            world.spawnEntityInWorld(toDrop);
            item.stackSize = 0;
        }
    }
}
