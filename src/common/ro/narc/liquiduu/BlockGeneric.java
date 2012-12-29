package ro.narc.liquiduu;

import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockGeneric extends BlockContainer {
    public static final byte DATA_ACCELERATOR  = 0;
    public static final byte DATA_ELECTROLYZER = 1;

    public static final int TI_ACCELERATOR_FRONT  =   0;
    public static final int TI_ACCELERATOR_SIDE   =   1;
    public static final int TI_ELECTROLYZER_FRONT =  16;
    public static final int TI_MACHINEFACE_EU_IN  =  17;
    public static final int TI_MACHINEFACE_EU_OUT =  18;
    public static final int TI_MACHINEFACE_WATER  =  19;
    public static final int TI_MACHINEFACE_EWATER =  20;
    public static final int TI_MACHINEFACE_NONE   =  21;
    public static final int TI_BROKEN             = 255;

    public static final int EID_FACING = 0;

    public BlockGeneric(int id, Material material) {
        super(id, material);
        this.setBlockName("liquiduu.block.generic");
        this.setHardness(2.0F);
    }

    public BlockGeneric(int id) {
        this(id, Material.iron);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(int id, CreativeTabs tab, List tabContents) {
        if(!tabContents.contains(LiquidUU.accelerator)) {
            tabContents.add(LiquidUU.accelerator);
        }
        if(!tabContents.contains(LiquidUU.electrolyzer)) {
            tabContents.add(LiquidUU.electrolyzer);
        }
    }

    @Override
    public String getTextureFile() {
        return "/liquiduu-gfx/blocks.png";
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int data) {
        if(data == DATA_ACCELERATOR) {
            return new TileEntityAccelerator();
        }
        else if(data == DATA_ELECTROLYZER) {
            return new TileEntityElectrolyzer();
        }

        return null;
    }

    @Override
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if(te instanceof IHasMachineFaces) {
            MachineFace mf = ((IHasMachineFaces)te).getMachineFace(side);

            if(mf != null) {
                return mf.textureIndex;
            }
            else {
                return TI_BROKEN;
            }
        }

        return getBlockTextureFromSideAndMetadata(side, world.getBlockMetadata(x, y, z));
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int data) {
        if(data == DATA_ACCELERATOR) {
            if(side == 3) { return MachineFace.AcceleratorFront.textureIndex; }

            return MachineFace.AcceleratorSide.textureIndex;
        }
        else if(data == DATA_ELECTROLYZER) {
            if(side == 3) { return MachineFace.ElectrolyzerFront.textureIndex; }

            return MachineFace.None.textureIndex;
        }

        return TI_BROKEN;
    }

    @Override
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
                    accelerator.setFacing((short) 3);
                    break;
                case 1:
                    accelerator.setFacing((short) 4);
                    break;
                case 2:
                    accelerator.setFacing((short) 2);
                    break;
                case 3:
                    accelerator.setFacing((short) 5);
                    break;
            }
        }
    }

    @Override
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

    @Override
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
            world.markBlockForUpdate(x, y, z);
        }
    }

    @Override
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

            EntityItem toDrop = new EntityItem(world, x + rx, y + ry, z + rz, item.copy());
            world.spawnEntityInWorld(toDrop);
            item.stackSize = 0;
        }
    }
}
