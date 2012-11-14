package ro.narc.liquiduu;

import cpw.mods.fml.common.Side;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import java.util.Random;

public class BlockGeneric extends BlockContainer {
    public static final byte DATA_ACCELERATOR = 0;

    public static final int TI_ACCELERATOR_FACE = 0;
    public static final int TI_ACCELERATOR_SIDE = 1;
    public static final int TI_BROKEN = 2;

    public static final int EID_FACING = 0;

    public BlockGeneric(int id, Material material) {
        super(id, material);
    }

    public BlockGeneric(int id) {
        this(id, Material.iron);
    }

    public String getTextureFile() {
        return "/liquiduu-gfx/blocks.png";
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

    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof TileEntityAccelerator) {
            if(side == ((TileEntityAccelerator)te).getFacing()) {
                return TI_ACCELERATOR_FACE;
            }
            else {
                return TI_ACCELERATOR_SIDE;
            }
        }

        return TI_BROKEN;
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity) {
        if(LiquidUU.getSide() == Side.CLIENT) {
            return;
        }

        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(!(te instanceof TileEntityAccelerator)) {
            return;
        }

        TileEntityAccelerator accelerator = (TileEntityAccelerator)te;
        if(entity == null) {
            accelerator.setFacing((short) 2);
            return;
        }

        int pitch = Math.round(entity.rotationPitch);
        int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if(pitch >= 65) {
            accelerator.setFacing((short) 1);
        }
        else if(pitch <= -65) {
            accelerator.setFacing((short) 0);
        }
        else {
            switch(facing) {
                case 0:
                    accelerator.setFacing((short) 2);
                    break;
                case 1:
                    accelerator.setFacing((short) 5);
                    break;
                case 2:
                    accelerator.setFacing((short) 3);
                    break;
                case 3:
                    accelerator.setFacing((short) 4);
                    break;
            }
        }
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

    public void onBlockEventReceived(World world, int x, int y, int z, int eventID, int value) {
        if(LiquidUU.DEBUG_NETWORK) {
            System.out.println("BlockGeneric block event with world " + world + " at " + x + ", " + y + ", " + z + ", event " + eventID + " with data " + value);
        }

        TileEntity te = world.getBlockTileEntity(x, y, z);
        if(!(te instanceof TileEntityAccelerator)) {
            // Should never happen, but for sanity's sake:
            super.onBlockEventReceived(world, x, y, z, eventID, value);
            return;
        }

        if(eventID == EID_FACING) {
            ((TileEntityAccelerator)te).setFacing((short)value);
            world.markBlockNeedsUpdate(x, y, z);
        }
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
