package ro.narc.liquiduu;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerElectrolyzer extends Container {
    public TileEntityElectrolyzer electrolyzer;

    public ContainerElectrolyzer(IInventory inventory, TileEntityElectrolyzer electrolyzer) {
        this.electrolyzer = electrolyzer;

        bindPlayerInventory(inventory);
    }

    public void bindPlayerInventory(IInventory inventory) {
        // main 9x3 slot inventory at 8, 84; inventory slots 9-35; container slots 4-30
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventory, 9 + (i * 9) + j,
                    8 + (j * 18), 84 + (i * 18)));
            }
        }

        // hotbar at 8, 142; inventory slots 0-8; container slots 31-39
        for(int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventory, i,
                8 + (i * 18), 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < crafters.size(); i++) {
            electrolyzer.sendGUINetworkData(this, (ICrafting)crafters.get(i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int key, int value) {
        electrolyzer.getGUINetworkData(key, value);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return electrolyzer.isUseableByPlayer(player);
    }
}
